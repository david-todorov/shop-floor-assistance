package com.shopfloor.backend.services.interfaces;

import com.shopfloor.backend.api.transferobjects.operators.OperatorExecutionTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorForecastTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;

import java.util.List;

/**
 * OperatorService is an interface for the OperatorController.
 * It provides methods to perform operator operations.
 * @author David Todorov (https://github.com/david-todorov)
 */
public interface OperatorService {

    /**
     * Retrieves all orders.
     * @return a list of all operator orders.
     */
    List<OperatorOrderTO> getAllOrders();

    /**
     * Retrieves an order by its ID.
     * @param id the ID of the order to retrieve.
     * @return the retrieved operator order.
     */
    OperatorOrderTO getOrder(Long id);

    /**
     * Starts the execution of an order.
     * @param orderId the ID of the order to start execution for.
     * @return the execution details of the started order.
     */
    OperatorExecutionTO startExecution(Long orderId);

    /**
     * Finishes the execution of an order.
     * @param executionId the ID of the execution to finish.
     * @return the execution details of the finished order.
     */
    OperatorExecutionTO finishExecution(Long executionId);

    /**
     * Aborts the execution of an order.
     * @param executionId the ID of the execution to abort.
     * @return the execution details of the aborted order.
     */
    OperatorExecutionTO abortExecution(Long executionId);

    /**
     * Retrieves the forecast for an order.
     * @param orderId the ID of the order to retrieve the forecast for.
     * @return the forecast details of the order.
     */
    OperatorForecastTO getForecast(Long orderId);
}

