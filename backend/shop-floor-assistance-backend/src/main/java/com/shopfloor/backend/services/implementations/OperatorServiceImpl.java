package com.shopfloor.backend.services.implementations;

import com.shopfloor.backend.api.mappers.OperatorTOMapper;
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
import com.shopfloor.backend.services.interfaces.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of the OperatorService interface.
 * This tests handles operations related to orders and executions for operators.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class OperatorServiceImpl implements OperatorService {

    /**
     * Mapper to convert between transfer objects and database objects for operators.
     */
    private final OperatorTOMapper operatorTOMapper;

    /**
     * Repository to manage order data.
     */
    private final OrderRepository orderRepository;

    /**
     * Repository to manage execution data.
     */
    private final ExecutionRepository executionRepository;

    /**
     * Mapper to initialize and update execution database objects.
     */
    private final ExecutionDBOMapper executionDBOMapper;

    /**
     * Constructs an OperatorServiceImpl with the specified dependencies.
     *
     * @param operatorTOMapper the mapper to convert between transfer objects and database objects
     * @param orderRepository the repository to manage order data
     * @param executionRepository the repository to manage execution data
     * @param executionDBOMapper the mapper to initialize and update execution database objects
     */
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

    /**
     * Retrieves all orders.
     *
     * @return a list of OperatorOrderTO representing all orders
     */
    @Override
    public List<OperatorOrderTO> getAllOrders() {
        return this.operatorTOMapper.toOrderTOs(this.orderRepository.findAll());
    }

    /**
     * Retrieves a specific order by its ID.
     *
     * @param id the ID of the order to retrieve
     * @return an OperatorOrderTO representing the order
     * @throws OrderNotFoundException if the order is not found
     */
    @Override
    public OperatorOrderTO getOrder(Long id) {
        OrderDBO orderDBO = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        return this.operatorTOMapper.toOrderTO(orderDBO);
    }

    /**
     * Starts a new execution for a specific order.
     *
     * @param orderId the ID of the order to start execution for
     * @return an OperatorExecutionTO representing the started execution
     * @throws OrderNotFoundException if the order is not found
     */
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

    /**
     * Finishes an existing execution.
     *
     * @param executionId the ID of the execution to finish
     * @return an OperatorExecutionTO representing the finished execution
     * @throws ExecutionNotFoundException if the execution is not found
     */
    @Override
    public OperatorExecutionTO finishExecution(Long executionId) {
        Long finisherId = AuthenticatedUserDetails.getCurrentUserId();

        ExecutionDBO executionDBO = this.executionRepository.findById(executionId)
                .orElseThrow(ExecutionNotFoundException::new);

        this.executionDBOMapper.finishExecutionDBO(executionDBO, finisherId);
        executionDBO = this.executionRepository.save(executionDBO);

        return this.operatorTOMapper.toExecutionTO(executionDBO);
    }

    /**
     * Aborts an existing execution.
     *
     * @param executionId the ID of the execution to abort
     * @return an OperatorExecutionTO representing the aborted execution
     * @throws ExecutionNotFoundException if the execution is not found
     */
    @Override
    public OperatorExecutionTO abortExecution(Long executionId) {
        ExecutionDBO executionDBO = this.executionRepository.findById(executionId)
                .orElseThrow(ExecutionNotFoundException::new);

        this.executionDBOMapper.abortExecutionDBO(executionDBO);
        executionDBO = this.executionRepository.save(executionDBO);

        return this.operatorTOMapper.toExecutionTO(executionDBO);
    }

    /**
     * Retrieves the forecast for a specific order.
     *
     * @param orderId the ID of the order to get the forecast for
     * @return an OperatorForecastTO representing the forecast
     * @throws OrderNotFoundException if the order is not found
     */
    @Override
    public OperatorForecastTO getForecast(Long orderId) {
        OrderDBO orderDBO = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
        OperatorForecastTO forecastTO = new OperatorForecastTO();
        forecastTO.setTotalTimeRequired(orderDBO.getTotalTimeRequired());

        return forecastTO;
    }
}
