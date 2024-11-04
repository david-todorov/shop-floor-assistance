package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;
import com.shopfloor.backend.services.OperatorService;
import com.shopfloor.backend.services.OperatorServiceImpl;
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
@RequestMapping("/operator/orders")
public class OperatorController {

    private OperatorService operatorService;

    @Autowired
    public OperatorController(OperatorServiceImpl operatorService) {
        this.operatorService = operatorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OperatorOrderTO> getAllOrders() {
        return this.operatorService.getAllOrders();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OperatorOrderTO getOrderById(@PathVariable Long id) {
        return this.operatorService.getOrder(id);
    }

}
