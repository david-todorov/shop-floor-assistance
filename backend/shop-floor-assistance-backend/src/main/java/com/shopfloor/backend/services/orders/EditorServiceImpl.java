package com.shopfloor.backend.services.orders;

import com.shopfloor.backend.api.TOMapper;
import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.services.database.DBOInitializerMapper;
import com.shopfloor.backend.services.database.DBOUpdaterMapper;
import com.shopfloor.backend.services.database.exceptions.*;
import com.shopfloor.backend.services.database.objects.*;
import com.shopfloor.backend.services.database.repositories.OrderRepository;
import com.shopfloor.backend.services.database.repositories.UserRepository;
import com.shopfloor.backend.services.security.authentication.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is where the concrete implementations of EditorService is
 * Of course implement all the needed methods
 * But do not be afraid to extract common logic in private methods
 * The number of public methods should be the same as in EditorService
 * If you implement something public here and NOT declare it in EditorService
 * The controller EditorController can NOT see it
 * Have fun
 */
@Component
public class EditorServiceImpl implements EditorService {

    private final DBOInitializerMapper dboInitializerMapper;
    private final DBOUpdaterMapper dboUpdaterMapper;
    private final TOMapper toMapper;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public EditorServiceImpl(DBOInitializerMapper dboInitializerMapper,
                             TOMapper toMapper,
                             OrderRepository orderRepository,
                             JwtService jwtService,
                             UserRepository userRepository,
                             DBOUpdaterMapper dboUpdaterMapper) {
        this.dboInitializerMapper = dboInitializerMapper;
        this.dboUpdaterMapper = dboUpdaterMapper;
        this.toMapper = toMapper;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public List<OrderTO> getAllOrderAsTOs() {
        return this.toMapper.toOrderTOs(this.orderRepository.findAll());
    }

    @Override
    public OrderTO addOrder(OrderTO newOrderTO, String authorizationHeader) {

        if (newOrderTO.getOrderNumber() == null) {
            throw new OrderNotIdentifiableException();
        }
        Long creatorId = getIdFromHeader(authorizationHeader);

        String orderNumber = newOrderTO.getOrderNumber();
        if (getOrderIfExists(orderNumber) != null) {
            throw new OrderExistsException();
        }

        OrderDBO newOrderDBO = this.dboInitializerMapper.toOrderDBO(newOrderTO, creatorId);

        return this.toMapper.toOrderTO(this.orderRepository.save(newOrderDBO));
    }

    @Transactional
    public OrderTO updateOrder(Long id, OrderTO updatedOrderTO, String authorizationHeader) {

        Long updaterId = getIdFromHeader(authorizationHeader);

        String orderNumber = updatedOrderTO.getOrderNumber();

        if (orderNumber == null || id == null) {
            throw new OrderNotIdentifiableException();
        }

        // Fetch the existing order by ID
        OrderDBO existingOrderDBO = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotExistsException());

        // Check if another order has the same order number
        if (orderRepository.existsByOrderNumberAndIdNot(orderNumber, id)) {
            throw new OrderNumberExistsException();
        }

        // Update the order details
        this.dboUpdaterMapper.copyOrderDboFrom(existingOrderDBO, updatedOrderTO, updaterId);

        // Save the updated order and return the result
        return this.toMapper.toOrderTO(this.orderRepository.save(existingOrderDBO));
    }

    @Override
    public void deleteOrder(Long orderId) {
        OrderDBO toDeleteOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotExistsException());
        this.orderRepository.delete(toDeleteOrder);
    }

    private OrderDBO getOrderIfExists(String orderNumber) {
        return this.orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }

    private Long getIdFromHeader(String authorizationHeader) {
        return this.jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);
    }

}
