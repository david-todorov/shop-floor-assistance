package com.shopfloor.backend.services;

import com.shopfloor.backend.api.transferobjects.mappers.OperatorTOMapper;
import com.shopfloor.backend.api.transferobjects.operators.OperatorExecutionTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorForecastTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.database.exceptions.ExecutionNotFoundException;
import com.shopfloor.backend.database.exceptions.OrderNotFoundException;
import com.shopfloor.backend.database.mappers.ExecutionDBOMapper;
import com.shopfloor.backend.database.objects.ExecutionDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.repositories.ExecutionRepository;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.security.AuthenticatedUserDetails;
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
    private final ExecutionRepository executionRepository;

    private final ExecutionDBOMapper executionDBOMapper;

    @Autowired
    public OperatorServiceImpl(OperatorTOMapper operatorTOMapper,
                               OrderRepository orderRepository,
                               ExecutionRepository executionRepository,
                               ExecutionDBOMapper executionDBOMapper) {
        this.operatorTOMapper = operatorTOMapper;
        this.orderRepository = orderRepository;
        this.executionRepository = executionRepository;
        this.executionDBOMapper = executionDBOMapper;
    }

    @Override
    public List<OperatorOrderTO> getAllOrders() {
        return this.operatorTOMapper.toOrderTOs(this.orderRepository.findAll());
    }

    @Override
    public OperatorOrderTO getOrder(Long id) {
        OrderDBO orderDBO = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        return this.operatorTOMapper.toOrderTO(orderDBO);
    }

    @Override
    public OperatorExecutionTO startExecution(Long orderId) {
        Long executorId = AuthenticatedUserDetails.getCurrentUserId();

        OrderDBO order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        ExecutionDBO execution = this.executionDBOMapper.initializeExecutionDBO(executorId);
        execution = this.executionRepository.save(execution);

        order.addExecution(execution);
        this.orderRepository.save(order);


        return this.operatorTOMapper.toExecutionTO(execution);
    }

    @Override
    public OperatorExecutionTO finishExecution(Long executionId) {
        Long finisherId = AuthenticatedUserDetails.getCurrentUserId();

        ExecutionDBO executionDBO = this.executionRepository.findById(executionId)
                .orElseThrow(ExecutionNotFoundException :: new);

        this.executionDBOMapper.finishExecutionDBO(executionDBO, finisherId);
        executionDBO = this.executionRepository.save(executionDBO);

        return this.operatorTOMapper.toExecutionTO(executionDBO);
    }

    @Override
    public OperatorExecutionTO abortExecution(Long executionId) {

        ExecutionDBO executionDBO = this.executionRepository.findById(executionId)
                .orElseThrow(ExecutionNotFoundException :: new);

        this.executionDBOMapper.abortExecutionDBO(executionDBO);
        executionDBO = this.executionRepository.save(executionDBO);

        return this.operatorTOMapper.toExecutionTO(executionDBO);
    }

    @Override
    public OperatorForecastTO getForecast(Long orderId) {
        OrderDBO orderDBO = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        OperatorForecastTO forecastTO = new OperatorForecastTO();
        forecastTO.setTotalTimeRequired(orderDBO.getTotalTimeRequired());

        return forecastTO;
    }

}
