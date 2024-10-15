package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.OrderTO;
import com.shopfloor.backend.services.database.exceptions.OrderAlreadyExistsException;
import com.shopfloor.backend.services.database.objects.OrderDBO;
import com.shopfloor.backend.services.database.objects.WorkflowDBO;
import com.shopfloor.backend.services.orders.EditorService;
import com.shopfloor.backend.services.orders.EditorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        try {
            return this.editorService.getAllOrderAsTOs();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

    }

    // POST /orders/editor
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderTO createOrder(@RequestBody OrderTO newOrder,@RequestHeader("Authorization") String authorizationHeader) {

        try {
            return this.editorService.addOrderToDatabase(newOrder, authorizationHeader);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    // PUT /orders/editor/{id}
    @PutMapping("/{id}")
    public ResponseEntity<String> updateWorkflow(@PathVariable Long id, @RequestBody WorkflowDBO workflowDBO) {
        // Logic to update an existing workflow
        return ResponseEntity.ok("Workflow updated");
    }

    // DELETE /orders/editor/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWorkflow(@PathVariable Long id) {
        // Logic to delete a workflow
        return ResponseEntity.ok("Workflow deleted");
    }

    // Additional methods for handling steps, etc.
}
