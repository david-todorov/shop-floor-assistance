package com.shopfloor.backend.services.orders;

import com.shopfloor.backend.api.TOMapper;
import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.services.database.DBOMapper;
import com.shopfloor.backend.services.database.exceptions.OrderAlreadyExistsException;
import com.shopfloor.backend.services.database.objects.OrderDBO;
import com.shopfloor.backend.services.database.repositories.OrderRepository;
import com.shopfloor.backend.services.security.authentication.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private DBOMapper dboMapper;
    private TOMapper toMapper;
    private OrderRepository orderRepository;
    private JwtService jwtService;

    @Autowired
    public EditorServiceImpl(DBOMapper dboMapper, TOMapper toMapper, OrderRepository orderRepository, JwtService jwtService) {
        this.dboMapper = dboMapper;
        this.toMapper = toMapper;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
    }

    public List<OrderTO> getAllOrderAsTOs() {
        return this.toMapper.toOrderTOs(this.orderRepository.findAll(), true);
    }

    public OrderTO addOrderToDatabase(OrderTO newOrderTO, String authorizationHeader) {

        if (this.orderRepository.existsByOrderNumber(newOrderTO.getOrderNumber())) {
            throw new OrderAlreadyExistsException();
        }

        Long creatorId = this.jwtService.extractUserIdFromAuthorizationHeader(authorizationHeader);

        OrderDBO newOrderDBO = this.dboMapper.toOrderDBO(newOrderTO, creatorId);

        return this.toMapper.toOrderTO(this.orderRepository.save(newOrderDBO), true);
    }
}
