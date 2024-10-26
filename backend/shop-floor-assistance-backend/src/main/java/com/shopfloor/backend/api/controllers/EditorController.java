package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
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

    @PutMapping("orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO updateOrder(@PathVariable Long id, @Valid @RequestBody EditorOrderTO updatedOrder) {
        return this.editorService.updateOrder(id, updatedOrder);
    }

    @DeleteMapping("orders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long id) {
        this.editorService.deleteOrder(id);
    }

    @GetMapping("orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO getOrder(@PathVariable Long id) {
        return this.editorService.getOrder(id);
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

    @PutMapping("products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditorProductTO updateProduct(@PathVariable Long id, @Valid @RequestBody EditorProductTO updatedProduct) {
        return this.editorService.updateProduct(id, updatedProduct);
    }

    @DeleteMapping("products/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        this.editorService.deleteProduct(id);
    }

    @GetMapping("products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EditorProductTO getProduct(@PathVariable Long id) {
        return this.editorService.getProduct(id);
    }

}
