package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.services.EditorService;
import com.shopfloor.backend.services.EditorServiceImpl;
import jakarta.validation.Valid;
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
    public List<EditorOrderTO> getAllOrders() {
        return this.editorService.getAllOrders();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EditorOrderTO createOrder(@Valid @RequestBody EditorOrderTO newOrder) {
        return this.editorService.addOrder(newOrder);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO updateOrder(@PathVariable Long id, @Valid @RequestBody EditorOrderTO updatedOrder) {
        return this.editorService.updateOrder(id, updatedOrder);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        this.editorService.deleteOrder(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO getAllOrders(@PathVariable Long id) {
        return this.editorService.getOrder(id);
    }

}
