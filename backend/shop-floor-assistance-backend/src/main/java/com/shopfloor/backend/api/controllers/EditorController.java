package com.shopfloor.backend.api.controllers;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.services.implementations.EditorServiceImpl;
import com.shopfloor.backend.services.interfaces.EditorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EditorController is a REST controller that handles editor-related requests.
 * It provides endpoints for editor operations.
 *
 * This controller uses the EditorService to perform the actual editor logic.
 * @author David Todorov (https://github.com/david-todorov)
 */
@RestController
@RequestMapping("/editor")
public class EditorController {

    private final EditorService editorService;

    /**
     * Constructs an EditorController with the specified EditorService implementation.
     *
     * @param editorService the implementation of EditorService to use
     */
    @Autowired
    public EditorController(EditorServiceImpl editorService) {
        this.editorService = editorService;
    }

    /**
     * Retrieves all orders.
     *
     * @return a list of EditorOrderTO objects
     */
    @GetMapping("orders")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorOrderTO> getOrder() {
        return this.editorService.getAllOrders();
    }

    /**
     * Creates a new order.
     *
     * @param newOrder the new order to create
     * @return the created EditorOrderTO object
     */
    @PostMapping("orders")
    @ResponseStatus(HttpStatus.CREATED)
    public EditorOrderTO createOrder(@Valid @RequestBody EditorOrderTO newOrder) {
        return this.editorService.addOrder(newOrder);
    }

    /**
     * Updates an existing order.
     *
     * @param orderId the ID of the order to update
     * @param updatedOrder the updated order details
     * @return the updated EditorOrderTO object
     */
    @PutMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO updateOrder(@PathVariable Long orderId, @Valid @RequestBody EditorOrderTO updatedOrder) {
        return this.editorService.updateOrder(orderId, updatedOrder);
    }

    /**
     * Deletes an order.
     *
     * @param orderId the ID of the order to delete
     */
    @DeleteMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable Long orderId) {
        this.editorService.deleteOrder(orderId);
    }

    /**
     * Retrieves a specific order by ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the retrieved EditorOrderTO object
     */
    @GetMapping("orders/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorOrderTO getOrder(@PathVariable Long orderId) {
        return this.editorService.getOrder(orderId);
    }

    /**
     * Retrieves all products.
     *
     * @return a list of EditorProductTO objects
     */
    @GetMapping("products")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorProductTO> getAllProducts() {
        return this.editorService.getAllProducts();
    }

    /**
     * Creates a new product.
     *
     * @param newProduct the new product to create
     * @return the created EditorProductTO object
     */
    @PostMapping("products")
    @ResponseStatus(HttpStatus.CREATED)
    public EditorProductTO createProduct(@Valid @RequestBody EditorProductTO newProduct) {
        return this.editorService.addProduct(newProduct);
    }

    /**
     * Updates an existing product.
     *
     * @param productId the ID of the product to update
     * @param updatedProduct the updated product details
     * @return the updated EditorProductTO object
     */
    @PutMapping("products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorProductTO updateProduct(@PathVariable Long productId, @Valid @RequestBody EditorProductTO updatedProduct) {
        return this.editorService.updateProduct(productId, updatedProduct);
    }

    /**
     * Deletes a product.
     *
     * @param productId the ID of the product to delete
     */
    @DeleteMapping("products/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long productId) {
        this.editorService.deleteProduct(productId);
    }

    /**
     * Retrieves a specific product by ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the retrieved EditorProductTO object
     */
    @GetMapping("products/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorProductTO getProduct(@PathVariable Long productId) {
        return this.editorService.getProduct(productId);
    }

    /**
     * Retrieves all equipment.
     *
     * @return a list of EditorEquipmentTO objects
     */
    @GetMapping("equipment")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorEquipmentTO> getAllEquipment() {
        return this.editorService.getAllEquipment();
    }

    /**
     * Creates new equipment.
     *
     * @param newEquipment the new equipment to create
     * @return the created EditorEquipmentTO object
     */
    @PostMapping("equipment")
    @ResponseStatus(HttpStatus.CREATED)
    public EditorEquipmentTO createEquipment(@Valid @RequestBody EditorEquipmentTO newEquipment) {
        return this.editorService.addEquipment(newEquipment);
    }

    /**
     * Updates existing equipment.
     *
     * @param equipmentId the ID of the equipment to update
     * @param updatedEquipment the updated equipment details
     * @return the updated EditorEquipmentTO object
     */
    @PutMapping("equipment/{equipmentId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorEquipmentTO updateEquipment(@PathVariable Long equipmentId, @Valid @RequestBody EditorEquipmentTO updatedEquipment) {
        return this.editorService.updateEquipment(equipmentId, updatedEquipment);
    }

    /**
     * Deletes equipment.
     *
     * @param equipmentId the ID of the equipment to delete
     */
    @DeleteMapping("equipment/{equipmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEquipment(@PathVariable Long equipmentId) {
        this.editorService.deleteEquipment(equipmentId);
    }

    /**
     * Retrieves specific equipment by ID.
     *
     * @param equipmentId the ID of the equipment to retrieve
     * @return the retrieved EditorEquipmentTO object
     */
    @GetMapping("equipment/{equipmentId}")
    @ResponseStatus(HttpStatus.OK)
    public EditorEquipmentTO getEquipment(@PathVariable Long equipmentId) {
        return this.editorService.getEquipment(equipmentId);
    }

    /**
     * Retrieves equipment suggestions.
     *
     * @return a list of suggested EditorEquipmentTO objects
     */
    @GetMapping("equipment/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorEquipmentTO> getEquipmentSuggestions() {
        int limit = 5;
        return this.editorService.getEquipmentSuggestions(limit);
    }

    /**
     * Retrieves product suggestions.
     *
     * @return a list of suggested EditorProductTO objects
     */
    @GetMapping("products/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorProductTO> getProductsSuggestions() {
        int limit = 5;
        return this.editorService.getProductsSuggestions(limit);
    }

    /**
     * Retrieves workflow suggestions based on the provided product.
     *
     * @param productAfter the product to base the suggestions on
     * @return a list of suggested EditorWorkflowTO objects
     */
    @PostMapping("workflows/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorWorkflowTO> getWorkflowsSuggestions(@Valid @RequestBody EditorProductTO productAfter) {
        return this.editorService.getWorkflowsSuggestions(productAfter);
    }

    /**
     * Retrieves task suggestions based on the provided product.
     *
     * @param productAfter the product to base the suggestions on
     * @return a list of suggested EditorTaskTO objects
     */
    @PostMapping("tasks/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorTaskTO> getTasksSuggestions(@Valid @RequestBody EditorProductTO productAfter) {
        return this.editorService.getTasksSuggestions(productAfter);
    }

    /**
     * Retrieves item suggestions based on the provided product.
     *
     * @param productAfter the product to base the suggestions on
     * @return a list of suggested EditorItemTO objects
     */
    @PostMapping("items/suggestions")
    @ResponseStatus(HttpStatus.OK)
    public List<EditorItemTO> getItemsSuggestions(@Valid @RequestBody EditorProductTO productAfter) {
        return this.editorService.getItemsSuggestions(productAfter);
    }
}
