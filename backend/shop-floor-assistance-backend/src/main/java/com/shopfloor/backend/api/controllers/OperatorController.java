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
 *                            PLEASE READ
 * This is a blueprint for controller which uses OperatorServiceImpl
 * In our case we use it through an interface called OperatorService
 * Any logic should be in declared in OperatorService interface first
 * Then OperatorServiceImpl implements it
 * Finally OperatorController uses it
 *                              IMPORTANT
 * No concrete implementations here, just use the "operatorService"
 * This ensures that the implementation is flexible and scalable
 * Thank you for your time, now go implement
 */
@RestController
@RequestMapping("/operator")
public class OperatorController {

    private final OperatorService operatorService;

    @Autowired
    public OperatorController(OperatorServiceImpl operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping("orders")
    @ResponseStatus(HttpStatus.OK)
    public List<OperatorOrderTO> getAllOrders() {
        return this.operatorService.getAllOrders();
    }

    @GetMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OperatorOrderTO getOrderById(@PathVariable Long orderId) {
        return this.operatorService.getOrder(orderId);
    }

    @PostMapping("start/{orderId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OperatorExecutionTO startOrder(@PathVariable Long orderId) {
        return this.operatorService.startExecution(orderId);
    }

    @PutMapping("finish/{executionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OperatorExecutionTO finishOrder(@PathVariable Long executionId) {
        return this.operatorService.finishExecution(executionId);
    }

    @PutMapping("abort/{executionId}")
    @ResponseStatus(HttpStatus.CREATED)
    public OperatorExecutionTO abortOrder(@PathVariable Long executionId) {
        return this.operatorService.abortExecution(executionId);
    }

    @GetMapping("forecast/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OperatorForecastTO getForecastByOrderId(@PathVariable Long orderId) {
        return this.operatorService.getForecast(orderId);
    }

}
