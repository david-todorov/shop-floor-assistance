package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.services.interfaces.EditorService;
import com.shopfloor.backend.services.implementations.EditorServiceImpl;
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
@RequestMapping("/editor")
public class EditorController {

    private final EditorService editorService;

    @Autowired
    public EditorController(EditorServiceImpl editorService) {
        this.editorService = editorService;
    }

    /**
     * ORDERS
     */
    @GetMapping("orders")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorOrderTO> getOrder() {
        return this.editorService.getAllOrders();
    }

    @PostMapping("orders")
    @ResponseStatus(HttpStatus.CREATED)
    public EditorOrderTO createOrder(@Valid @RequestBody EditorOrderTO newOrder) {
        return this.editorService.addOrder(newOrder);
    }

    @PutMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO updateOrder(@PathVariable Long orderId, @Valid @RequestBody EditorOrderTO updatedOrder) {
        return this.editorService.updateOrder(orderId, updatedOrder);
    }

    @DeleteMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long orderId) {
        this.editorService.deleteOrder(orderId);
    }

    @GetMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO getOrder(@PathVariable Long orderId) {
        return this.editorService.getOrder(orderId);
    }

    /**
     * PRODUCTS
     */
    @GetMapping("products")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorProductTO> getAllProducts() {
        return this.editorService.getAllProducts();
    }

    @PostMapping("products")
    @ResponseStatus(HttpStatus.CREATED)
    public EditorProductTO createProduct(@Valid @RequestBody EditorProductTO newProduct) {
        return this.editorService.addProduct(newProduct);
    }

    @PutMapping("products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorProductTO updateProduct(@PathVariable Long productId, @Valid @RequestBody EditorProductTO updatedProduct) {
        return this.editorService.updateProduct(productId, updatedProduct);
    }

    @DeleteMapping("products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long productId) {
        this.editorService.deleteProduct(productId);
    }

    @GetMapping("products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorProductTO getProduct(@PathVariable Long productId) {
        return this.editorService.getProduct(productId);
    }

    /**
     * EQUIPMENT
     */
    @GetMapping("equipment")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorEquipmentTO> getAllEquipment() {
        return this.editorService.getAllEquipment();
    }

    @PostMapping("equipment")
    @ResponseStatus(HttpStatus.CREATED)
    public EditorEquipmentTO createEquipment(@Valid @RequestBody EditorEquipmentTO newEquipment) {
        return this.editorService.addEquipment(newEquipment);
    }

    @PutMapping("equipment/{equipmentId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorEquipmentTO updateEquipment(@PathVariable Long equipmentId, @Valid @RequestBody EditorEquipmentTO updatedEquipment) {
        return this.editorService.updateEquipment(equipmentId, updatedEquipment);
    }

    @DeleteMapping("equipment/{equipmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEquipment(@PathVariable Long equipmentId) {
        this.editorService.deleteEquipment(equipmentId);
    }

    @GetMapping("equipment/{equipmentId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorEquipmentTO getEquipment(@PathVariable Long equipmentId) {
        return this.editorService.getEquipment(equipmentId);
    }

    /**
     * SUGGESTIONS
     */
    @GetMapping("equipment/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorEquipmentTO> getEquipmentSuggestions() {
        int limit = 5;
        return this.editorService.getEquipmentSuggestions(limit);
    }

    @GetMapping("products/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorProductTO> getProductsSuggestions() {
        int limit = 5;
        return this.editorService.getProductsSuggestions(limit);
    }

    @PostMapping("workflows/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorWorkflowTO> getWorkflowsSuggestions(@Valid @RequestBody EditorProductTO productAfter) {
        return this.editorService.getWorkflowsSuggestions(productAfter);
    }

    @PostMapping("tasks/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorTaskTO> getTasksSuggestions(@Valid @RequestBody EditorProductTO productAfter) {
        return this.editorService.getTasksSuggestions(productAfter);
    }

    @PostMapping("items/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorItemTO> getItemsSuggestions(@Valid @RequestBody EditorProductTO productAfter) {
        return this.editorService.getItemsSuggestions(productAfter);
    }
}
