package com.shopfloor.backend.services;

import com.shopfloor.backend.api.TOMapper;
import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.database.exceptions.*;
import com.shopfloor.backend.database.mappers.DBOInitializerMapper;
import com.shopfloor.backend.database.mappers.DBOUpdaterMapper;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.database.repositories.UserRepository;
import com.shopfloor.backend.security.JwtService;
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
    public List<OrderTO> getAllOrders() {
        return this.toMapper.toOrderTOs(this.orderRepository.findAll());
    }

    @Override
    public OrderTO addOrder(OrderTO newOrderTO, String authorizationHeader) {
        validateOrder(newOrderTO);
        validateAuthorizationHeader(authorizationHeader);

        Long creatorId = this.jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);

        if (getOrderIfExists(newOrderTO.getOrderNumber()) != null) {
            throw new DuplicatedOrderException();
        }

        OrderDBO newOrderDBO = this.dboInitializerMapper.toOrderDBO(newOrderTO, creatorId);

        return this.toMapper.toOrderTO(this.orderRepository.save(newOrderDBO));
    }

    @Transactional
    public OrderTO updateOrder(Long orderId, OrderTO updatedOrderTO, String authorizationHeader) {
        validateOrderId(orderId);
        validateOrder(updatedOrderTO);
        validateAuthorizationHeader(authorizationHeader);

        Long updaterId = this.jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);

        OrderDBO existingOrderDBO = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        if (orderRepository.existsByOrderNumberAndIdNot(updatedOrderTO.getOrderNumber(), orderId)) {
            throw new DuplicatedOrderException();
        }

        this.dboUpdaterMapper.copyOrderDboFrom(existingOrderDBO, updatedOrderTO, updaterId);

        return this.toMapper.toOrderTO(this.orderRepository.save(existingOrderDBO));
    }

    @Override
    public OrderTO getOrder(Long orderId) {
        validateOrderId(orderId);

        OrderDBO orderDBO = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        return this.toMapper.toOrderTO(orderDBO);
    }

    @Override
    public void deleteOrder(Long orderId) {
        validateOrderId(orderId);

        OrderDBO toDeleteOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        this.orderRepository.delete(toDeleteOrder);
    }

    private void validateOrderId(Long orderId) {
        if (orderId == null) {
            throw new MissingOrderIdException();
        }
    }

    private void validateOrder(OrderTO orderTO) {
        if (orderTO == null) {
            throw new MissingOrderException();
        }
        if (orderTO.getOrderNumber() == null) {
            throw new MissingOrderNumberException();
        }
    }

    private void validateAuthorizationHeader(String authorizationHeader) {
        if (authorizationHeader == null) {
            throw new MissingAuthorizationHeaderException();
        }
    }

    private OrderDBO getOrderIfExists(String orderNumber) {
        return this.orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }

}
