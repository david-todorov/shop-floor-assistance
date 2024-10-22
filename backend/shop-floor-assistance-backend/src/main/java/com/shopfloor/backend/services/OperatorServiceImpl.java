package com.shopfloor.backend.services;

import com.shopfloor.backend.api.transferobjects.mappers.OperatorTOMapper;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.database.exceptions.OrderNotFoundException;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is where the concrete implementations of OperatorService is
 * Of course implement all the needed methods
 * But do not be afraid to extract common logic in private methods
 * The number of public methods should be the same as in OperatorService
 * If you implement something public here and NOT declare it in OperatorService
 * The controller OperatorController can NOT see it
 * Have fun
 */
@Component
public class OperatorServiceImpl implements OperatorService {

    private final OperatorTOMapper operatorTOMapper;
    private final OrderRepository orderRepository;

    @Autowired
    public OperatorServiceImpl(OperatorTOMapper operatorTOMapper, OrderRepository orderRepository) {
        this.operatorTOMapper = operatorTOMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OperatorOrderTO> getAllOrders() {
        return this.operatorTOMapper.toOrderTOs(this.orderRepository.findAll());
    }

    @Override
    public OperatorOrderTO getOrder(long id) {
        OrderDBO orderDBO = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException());

        return this.operatorTOMapper.toOrderTO(orderDBO);
    }
}
