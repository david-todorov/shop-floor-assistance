package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.services.EditorService;
import com.shopfloor.backend.services.EditorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PLEASE READ
 * This is a blueprint for controller which uses EditorServiceImpl
 * In our case we use it through an interface called EditorService
 * Any logic should be in declared in EditorService interface first
 * Then EditorServiceImpl implements it
 * Finally EditorController uses it
 * IMPORTANT
 * No concrete implementations here, just use the "editorService"
 * This ensures that the implementation is flexible and scalable
 * Thank you for your time, now go implement
 **/
@RestController
@RequestMapping("/editor/orders")
public class EditorController {

    private final EditorService editorService;

    @Autowired
    public EditorController(EditorServiceImpl editorService) {
        this.editorService = editorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderTO> getAllOrders() {
        return this.editorService.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderTO createOrder(@RequestBody OrderTO newOrder, @RequestHeader("Authorization") String authorizationHeader) {
        return this.editorService.addOrder(newOrder, authorizationHeader);
    }

    @PutMapping("/{id}")
    public OrderTO updateOrder(@PathVariable Long id, @RequestBody OrderTO updatedOrder, @RequestHeader("Authorization") String authorizationHeader) {
        return this.editorService.updateOrder(id, updatedOrder, authorizationHeader);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        this.editorService.deleteOrder(id);
    }

}
