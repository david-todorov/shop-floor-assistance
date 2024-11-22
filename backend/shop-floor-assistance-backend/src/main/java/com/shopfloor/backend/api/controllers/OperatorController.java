package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.operators.OperatorExecutionTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorForecastTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.services.implementations.OperatorServiceImpl;
import com.shopfloor.backend.services.interfaces.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * OperatorController is a REST controller that handles operator-related requests.
 * It provides endpoints for operator operations.
 *
 * This controller uses the OperatorService to perform the actual operator logic.
 * @author David Todorov (https://github.com/david-todorov)
 */
@RestController
@RequestMapping("/operator")
public class OperatorController {

    private final OperatorService operatorService;

    /**
     * Constructs an OperatorController with the specified OperatorService implementation.
     *
     * @param operatorService the implementation of OperatorService to use
     */
    @Autowired
    public OperatorController(OperatorServiceImpl operatorService) {
        this.operatorService = operatorService;
    }

    /**
     * Retrieves all orders.
     *
     * @return a list of OperatorOrderTO objects
     */
    @GetMapping("orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OperatorOrderTO> getAllOrders() {
        return this.operatorService.getAllOrders();
    }

    /**
     * Retrieves a specific order by ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the retrieved OperatorOrderTO object
     */
    @GetMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OperatorOrderTO getOrderById(@PathVariable Long orderId) {
        return this.operatorService.getOrder(orderId);
    }

    /**
     * Starts an order execution.
     *
     * @param orderId the ID of the order to start
     * @return the started OperatorExecutionTO object
     */
    @PostMapping("start/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OperatorExecutionTO startOrder(@PathVariable Long orderId) {
        return this.operatorService.startExecution(orderId);
    }

    /**
     * Finishes an order execution.
     *
     * @param executionId the ID of the execution to finish
     * @return the finished OperatorExecutionTO object
     */
    @PutMapping("finish/{executionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OperatorExecutionTO finishOrder(@PathVariable Long executionId) {
        return this.operatorService.finishExecution(executionId);
    }

    /**
     * Aborts an order execution.
     *
     * @param executionId the ID of the execution to abort
     * @return the aborted OperatorExecutionTO object
     */
    @PutMapping("abort/{executionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OperatorExecutionTO abortOrder(@PathVariable Long executionId) {
        return this.operatorService.abortExecution(executionId);
    }

    /**
     * Retrieves the forecast for a specific order by ID.
     *
     * @param orderId the ID of the order to retrieve the forecast for
     * @return the retrieved OperatorForecastTO object
     */
    @GetMapping("forecast/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OperatorForecastTO getForecastByOrderId(@PathVariable Long orderId) {
        return this.operatorService.getForecast(orderId);
    }

}
