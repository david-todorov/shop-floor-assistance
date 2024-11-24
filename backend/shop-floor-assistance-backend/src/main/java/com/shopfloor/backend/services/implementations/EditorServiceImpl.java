package com.shopfloor.backend.services.implementations;

import com.shopfloor.backend.api.mappers.EditorTOMapper;
import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.database.exceptions.*;
import com.shopfloor.backend.database.mappers.EquipmentDBOMapper;
import com.shopfloor.backend.database.mappers.OrderDBOMapper;
import com.shopfloor.backend.database.mappers.ProductDBOMapper;
import com.shopfloor.backend.database.objects.EquipmentDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.ProductDBO;
import com.shopfloor.backend.database.repositories.EquipmentRepository;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.database.repositories.ProductRepository;
import com.shopfloor.backend.security.AuthenticatedUserDetails;
import com.shopfloor.backend.services.interfaces.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the EditorService interface.
 * Provides methods for retrieving and manipulating editor data.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Component
public class EditorServiceImpl implements EditorService {

    /**
     * Mapper for OrderDBO objects.
     */
    private final OrderDBOMapper orderDBOMapper;

    /**
     * Mapper for ProductDBO objects.
     */
    private final ProductDBOMapper productDBOMapper;

    /**
     * Mapper for EquipmentDBO objects.
     */
    private final EquipmentDBOMapper equipmentDBOMapper;

    /**
     * Mapper for editor transfer objects.
     */
    private final EditorTOMapper editorToMapper;

    /**
     * Repository for OrderDBO objects.
     */
    private final OrderRepository orderRepository;

    /**
     * Repository for ProductDBO objects.
     */
    private final ProductRepository productRepository;

    /**
     * Repository for EquipmentDBO objects.
     */
    private final EquipmentRepository equipmentRepository;

    /**
     * Constructs an EditorServiceImpl with the specified dependencies.
     *
     * @param orderDBOMapper the mapper for OrderDBO objects
     * @param productDBOMapper the mapper for ProductDBO objects
     * @param equipmentDBOMapper the mapper for EquipmentDBO objects
     * @param editorToMapper the mapper for editor transfer objects
     * @param orderRepository the repository for OrderDBO objects
     * @param productRepository the repository for ProductDBO objects
     * @param equipmentRepository the repository for EquipmentDBO objects
     */
    @Autowired
    public EditorServiceImpl(OrderDBOMapper orderDBOMapper,
                             ProductDBOMapper productDBOMapper,
                             EquipmentDBOMapper equipmentDBOMapper,
                             EditorTOMapper editorToMapper,
                             OrderRepository orderRepository,
                             ProductRepository productRepository,
                             EquipmentRepository equipmentRepository) {
        this.orderDBOMapper = orderDBOMapper;
        this.productDBOMapper = productDBOMapper;
        this.equipmentDBOMapper = equipmentDBOMapper;
        this.editorToMapper = editorToMapper;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.equipmentRepository = equipmentRepository;
    }

    /**
     * Retrieves a list of equipment suggestions.
     *
     * @param numberOfEquipments the number of equipment suggestions to retrieve
     * @return a list of EditorEquipmentTO objects
     */
    @Override
    public List<EditorEquipmentTO> getEquipmentSuggestions(int numberOfEquipments) {

        // Limits the number of equipment suggestions to the specified number
        Pageable pageable = PageRequest.of(0, numberOfEquipments);

        // Retrieves the top referenced equipment from the database and maps them to EditorEquipmentTO objects
        return this.editorToMapper.toEquipmentTOs(this.equipmentRepository.findTopReferencedEquipment(pageable));
    }

    /**
     * Retrieves a list of product suggestions.
     *
     * @param numberOfProducts the number of product suggestions to retrieve
     * @return a list of EditorProductTO objects
     */
    @Override
    public List<EditorProductTO> getProductsSuggestions(int numberOfProducts) {

        // Limits the number of equipment suggestions to the specified number
        Pageable pageable = PageRequest.of(0, numberOfProducts);

        // Retrieves the top referenced products from the database and maps them to EditorProductTO objects
        return this.editorToMapper.toProductTOs(this.productRepository.findTopReferencedProducts(pageable));
    }

    /**
     * Retrieves a list of workflow suggestions based on the provided product.
     *
     * @param productAfter the product to base the workflow suggestions on
     * @return a list of EditorWorkflowTO objects
     */
    @Override
    public List<EditorWorkflowTO> getWorkflowsSuggestions(EditorProductTO productAfter) {
        List<EditorWorkflowTO> suggested = new ArrayList<>();
        Long productId = productAfter.getId();

        // Retrieve the product from the database or throw an exception if not found
        ProductDBO productAfterDBO = this.productRepository.findById(productId).orElse(null);
        if (productAfterDBO == null) {
            throw new ProductNotFoundException();
        }

        // Use streams to filter orders where 'productBefore' is null
        productAfterDBO.getOrdersAsAfterProduct().stream()
                .filter(ordersAsAfter -> ordersAsAfter.getBeforeProduct() == null)
                .forEach(ordersAsAfter -> this.editorToMapper.toWorkflowTOs(ordersAsAfter.getWorkflows())
                        .forEach(workflow -> {
                            nullifyWorkflowIds(workflow); // Set ID and nested IDs to null
                            suggested.add(workflow);
                        }));

        return suggested;
    }

    /**
     * Retrieves a list of task suggestions based on the provided product.
     *
     * @param productAfter the product to base the task suggestions on
     * @return a list of EditorTaskTO objects
     */
    @Override
    public List<EditorTaskTO> getTasksSuggestions(EditorProductTO productAfter) {
        List<EditorTaskTO> suggested = new ArrayList<>();
        Long productId = productAfter.getId();

        // Retrieve the product from the database or throw an exception if not found
        ProductDBO productAfterDBO = this.productRepository.findById(productId).orElse(null);
        if (productAfterDBO == null) {
            throw new ProductNotFoundException();
        }

        // Use streams to filter orders where 'productBefore' is null
        productAfterDBO.getOrdersAsAfterProduct().stream()
                .filter(ordersAsAfter -> ordersAsAfter.getBeforeProduct() == null)
                .forEach(ordersAsAfter -> ordersAsAfter.getWorkflows().forEach(
                        workflow -> this.editorToMapper.toTaskTOs(workflow.getTasks()) // Map tasks to EditorTaskTO
                                .forEach(task -> {
                                    nullifyTaskIds(task); // Set ID and nested IDs to null
                                    suggested.add(task);
                                })
                ));

        return suggested;
    }

    /**
     * Retrieves a list of item suggestions based on the provided product.
     *
     * @param productAfter the product to base the item suggestions on
     * @return a list of EditorItemTO objects
     */
    @Override
    public List<EditorItemTO> getItemsSuggestions(EditorProductTO productAfter) {
        List<EditorItemTO> suggested = new ArrayList<>();
        Long productId = productAfter.getId();

        // Retrieve the product from the database or throw an exception if not found
        ProductDBO productAfterDBO = this.productRepository.findById(productId).orElse(null);
        if (productAfterDBO == null) {
            throw new ProductNotFoundException();
        }

        // Use streams to filter out orders where 'productBefore' is null
        productAfterDBO.getOrdersAsAfterProduct().stream()
                .filter(ordersAsAfter -> ordersAsAfter.getBeforeProduct() == null)
                .forEach(ordersAsAfter -> ordersAsAfter.getWorkflows().forEach(
                        workflow -> workflow.getTasks().forEach(
                                task -> this.editorToMapper.toItemTOs(task.getItems()) // Map items to EditorItemTO
                                        .forEach(item -> {
                                            nullifyItemIds(item); // Set ID to null
                                            suggested.add(item);
                                        })
                        )
                ));

        return suggested;
    }

    /**
     * Retrieves a list of all equipment.
     *
     * @return a list of EditorEquipmentTO objects
     */
    @Override
    public List<EditorEquipmentTO> getAllEquipment() {

        // Retrieve all equipment from the database and map them to EditorEquipmentTO objects
        return this.editorToMapper.toEquipmentTOs(this.equipmentRepository.findAll());
    }

    /**
     * Retrieves a specific equipment by its ID.
     *
     * @param equipmentId the ID of the equipment to retrieve
     * @return an EditorEquipmentTO object
     */
    @Override
    public EditorEquipmentTO getEquipment(Long equipmentId) {

        // Retrieve the equipment from the database or throw an exception if not found
        EquipmentDBO equipmentDBO = this.equipmentRepository.findById(equipmentId).orElseThrow(EquipmentNotFoundException::new);

        // Map the EquipmentDBO to EditorEquipmentTO and return it
        return this.editorToMapper.toEquipmentTO(equipmentDBO);
    }

    /**
     * Adds a new equipment.
     *
     * @param newEditorEquipmentTO the new equipment to add
     * @return the added EditorEquipmentTO object
     */
    @Transactional
    @Override
    public EditorEquipmentTO addEquipment(EditorEquipmentTO newEditorEquipmentTO) {

        // Retrieve the ID of the user creating the equipment
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        // Check if another equipment exists with the same equipment number
        if (this.getEquipmentIfExists(newEditorEquipmentTO.getEquipmentNumber()) != null) {
            throw new DuplicateEquipmentException();
        }

        // Map the EditorEquipmentTO to EquipmentDBO and set the creator ID
        EquipmentDBO newEquipmentDBO = this.equipmentDBOMapper.initializeEquipmentDBOFrom(newEditorEquipmentTO, creatorId);

        // Save the new equipment to the database and map it back to EditorEquipmentTO
        return this.editorToMapper.toEquipmentTO(this.equipmentRepository.save(newEquipmentDBO));
    }

    /**
     * Updates an existing equipment.
     *
     * @param equipmentId the ID of the equipment to update
     * @param updatedEditorEquipmentTO the updated equipment data
     * @return the updated EditorEquipmentTO object
     */
    @Transactional
    @Override
    public EditorEquipmentTO updateEquipment(Long equipmentId, EditorEquipmentTO updatedEditorEquipmentTO) {

        // The ID of the user (updater)
        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        // Retrieve the existing equipment from the database or throw an exception if not found
        EquipmentDBO existingEquipmentDBO = this.equipmentRepository.findById(equipmentId).orElseThrow(EquipmentNotFoundException::new);

        // Check for duplicate equipment numbers
        if (this.equipmentRepository.existsByEquipmentNumberAndIdNot(updatedEditorEquipmentTO.getEquipmentNumber(), equipmentId)) {
            throw new DuplicateEquipmentException();
        }

        // Update the existing equipment with the new data
        this.equipmentDBOMapper.updateEquipmentDBOFrom(existingEquipmentDBO, updatedEditorEquipmentTO, updaterId);

        // Save the updated equipment to the database and map it back to EditorEquipmentTO
        return this.editorToMapper.toEquipmentTO(this.equipmentRepository.save(existingEquipmentDBO));
    }

    /**
     * Deletes an existing equipment.
     *
     * @param equipmentId the ID of the equipment to delete
     */
    @Transactional
    @Override
    public void deleteEquipment(Long equipmentId) {

        // Retrieve the equipment to be deleted from the database or throw an exception if not found
        EquipmentDBO toDeleteequipmentDBO = this.equipmentRepository.findById(equipmentId).orElseThrow(EquipmentNotFoundException::new);

        // Clear order references to the equipment to maintain bidirectional relationships
        toDeleteequipmentDBO.clearOrderReferences();

        // Finally, delete the equipment itself from the repository
        this.equipmentRepository.delete(toDeleteequipmentDBO);
    }

    /**
     * Retrieves a list of all products.
     *
     * @return a list of EditorProductTO objects
     */
    @Override
    public List<EditorProductTO> getAllProducts() {

        // Retrieve all products from the database and map them to EditorProductTO objects
        return this.editorToMapper.toProductTOs(this.productRepository.findAll());
    }

    /**
     * Retrieves a specific product by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return an EditorProductTO object
     */
    @Override
    public EditorProductTO getProduct(Long productId) {

        // Retrieve the product from the database or throw an exception if not found
        ProductDBO productDBO = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        // Map the ProductDBO to EditorProductTO and return it
        return this.editorToMapper.toProductTO(productDBO);
    }

    /**
     * Adds a new product.
     *
     * @param newEditorProductTO the new product to add
     * @return the added EditorProductTO object
     */
    @Transactional
    @Override
    public EditorProductTO addProduct(EditorProductTO newEditorProductTO) {

        // Retrieve the ID of the user creating the product
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        // Check if another product exists with the same product number
        if (getProductIfExists(newEditorProductTO.getProductNumber()) != null) {
            throw new DuplicateProductException();
        }

        // Map the EditorProductTO to ProductDBO and set the creator ID
        ProductDBO newProductDBO = this.productDBOMapper.initializeProductDBO(newEditorProductTO, creatorId);

        // Save the new product to the database and map it back to EditorProductTO
        return this.editorToMapper.toProductTO(this.productRepository.save(newProductDBO));
    }

    /**
     * Updates an existing product.
     *
     * @param productId the ID of the product to update
     * @param editorProductTO the updated product data
     * @return the updated EditorProductTO object
     */
    @Transactional
    @Override
    public EditorProductTO updateProduct(Long productId, EditorProductTO editorProductTO) {

        // The ID of the user (updater)
        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        // Retrieve the existing product from the database or throw an exception if not found
        ProductDBO existingProductDBO = this.productRepository.findById(editorProductTO.getId()).orElseThrow(ProductNotFoundException::new);

        // Check for duplicate product numbers
        if (productRepository.existsByProductNumberAndIdNot(editorProductTO.getProductNumber(), productId)) {
            throw new DuplicateProductException();
        }

        // Update the existing product with the new data
        this.productDBOMapper.updateProductDboFrom(existingProductDBO, editorProductTO, updaterId);

        // Save the updated product to the database and map it back to EditorProductTO
        return this.editorToMapper.toProductTO(this.productRepository.save(existingProductDBO));
    }

    /**
     * Deletes an existing product.
     *
     * @param productId the ID of the product to delete
     */
    @Transactional
    @Override
    public void deleteProduct(Long productId) {

        // Retrieve the product to be deleted from the database or throw an exception if not found
        ProductDBO toDeleteProductDBO = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        // Clear order references to the product to maintain bidirectional relationships
        toDeleteProductDBO.clearOrderReferences();

        // Finally, delete the product itself from the repository
        this.productRepository.delete(toDeleteProductDBO);
    }

    /**
     * Retrieves a list of all orders.
     *
     * @return a list of EditorOrderTO objects
     */
    @Override
    public List<EditorOrderTO> getAllOrders() {
        return this.editorToMapper.toOrderTOs(this.orderRepository.findAll());
    }

    /**
     * Retrieves a specific order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return an EditorOrderTO object
     */
    @Override
    public EditorOrderTO getOrder(Long orderId) {

        // Retrieve the order from the database or throw an exception if not found
        OrderDBO orderDBO = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        // Map the OrderDBO to EditorOrderTO and return it
        return this.editorToMapper.toOrderTO(orderDBO);
    }

    /**
     * Adds a new order.
     *
     * @param newEditorOrderTO the new order to add
     * @return the added EditorOrderTO object
     */
    @Transactional
    @Override
    public EditorOrderTO addOrder(EditorOrderTO newEditorOrderTO) {

        // Retrieve the ID of the user creating the order
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        // Check if another order exists with the same order number
        if (getOrderIfExists(newEditorOrderTO.getOrderNumber()) != null) {
            throw new DuplicatedOrderException();
        }

        // Retrieve the "before" product if provided
        ProductDBO productBefore = null;
        if (newEditorOrderTO.getProductBefore() != null) {
            productBefore = this.productRepository.findById(newEditorOrderTO.getProductBefore().getId())
                    .orElseThrow(ProductNotFoundException::new);
        }

        // Retrieve the "after" product if provided
        ProductDBO productAfter = null;
        if (newEditorOrderTO.getProductAfter() != null) {
            productAfter = this.productRepository.findById(newEditorOrderTO.getProductAfter().getId())
                    .orElseThrow(ProductNotFoundException::new);
        }

        // Map the EditorOrderTO to OrderDBO and set the creator ID
        OrderDBO newOrderDBO = this.orderDBOMapper.initializeOrderDBOFrom(newEditorOrderTO, creatorId);

        // Use helper methods to set products in OrderDBO and maintain bidirectional relationships
        newOrderDBO.setBeforeProduct(productBefore);
        newOrderDBO.setAfterProduct(productAfter);

        //Sets the equipment list
        List<EquipmentDBO> equipmentList = newEditorOrderTO.getEquipment().stream()
                .map(equipmentTO -> this.equipmentRepository.findById(equipmentTO.getId())
                        .orElseThrow(EquipmentNotFoundException::new))
                .toList();
        newOrderDBO.setEquipmentList(equipmentList);

        // Save the new order to the database
        newOrderDBO = this.orderRepository.save(newOrderDBO);

        //Sorting entities according to the ordering index, which comes directly from the order of the JSON request
        newOrderDBO.sortEntities();

        // Convert the saved OrderDBO back to EditorOrderTO to return to the frontend
        return this.editorToMapper.toOrderTO(newOrderDBO);
    }

    /**
     * Updates an existing order.
     *
     * @param orderId the ID of the order to update
     * @param updatedEditorOrderTO the updated order data
     * @return the updated EditorOrderTO object
     */
    @Transactional
    @Override
    public EditorOrderTO updateOrder(Long orderId, EditorOrderTO updatedEditorOrderTO) {
        // The ID of the user (updater)
        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        // Retrieve the existing order
        OrderDBO existingOrderDBO = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        // Check for duplicate order numbers
        if (orderRepository.existsByOrderNumberAndIdNot(updatedEditorOrderTO.getOrderNumber(), orderId)) {
            throw new DuplicatedOrderException();
        }

        // Retrieve and validate the optional productBefore
        ProductDBO productBefore = null;
        if (updatedEditorOrderTO.getProductBefore() != null) {
            productBefore = this.productRepository.findById(updatedEditorOrderTO.getProductBefore().getId())
                    .orElseThrow(ProductNotFoundException::new);
        }

        // Retrieve and validate the required productAfter
        ProductDBO productAfter = null;
        if (updatedEditorOrderTO.getProductAfter() != null) {
            productAfter = this.productRepository.findById(updatedEditorOrderTO.getProductAfter().getId())
                    .orElseThrow(ProductNotFoundException::new);
        }

        // Update beforeProduct association using the helper method
        // Update afterProduct association using the helper method
        existingOrderDBO.setBeforeProduct(productBefore);
        existingOrderDBO.setAfterProduct(productAfter);

        // Get the new equipment list and synchronize it, synchronization is RIGHT JOIN
        List<EquipmentDBO> equipmentList = updatedEditorOrderTO.getEquipment().stream()
                .map(equipmentTO -> this.equipmentRepository.findById(equipmentTO.getId())
                        .orElseThrow(EquipmentNotFoundException::new))
                .toList();
        existingOrderDBO.synchronizeEquipmentList(equipmentList);

        // Save the updated order
        existingOrderDBO = this.orderDBOMapper.updateOrderDBOFrom(existingOrderDBO, updatedEditorOrderTO, updaterId);
        existingOrderDBO = this.orderRepository.save(existingOrderDBO);

        // Sorting entities according to the ordering index, which comes directly from the order of the JSON request
        existingOrderDBO.sortEntities();

        // Convert the updated OrderDBO back to EditorOrderTO to return to the frontend
        return this.editorToMapper.toOrderTO(existingOrderDBO);
    }

    /**
     * Deletes an existing order.
     *
     * @param orderId the ID of the order to delete
     */
    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        // Retrieve the order to be deleted, or throw an exception if not found
        OrderDBO toDeleteOrderDBO = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        // Use helper methods to maintain bidirectional relationships with products
        toDeleteOrderDBO.clearBeforeProduct();
        toDeleteOrderDBO.clearAfterProduct();

        // Clear equipment references using the helper method to maintain bidirectional relationships
        toDeleteOrderDBO.clearEquipmentList();

        // Clear Execution references using helper method to maintain bidirectional relationships
        toDeleteOrderDBO.clearExecutions();

        // Finally, delete the order itself from the repository
        orderRepository.delete(toDeleteOrderDBO);
    }

    /**
     * Retrieves an order by its order number if it exists.
     *
     * @param orderNumber the order number to search for
     * @return the OrderDBO object if found, otherwise null
     */
    private OrderDBO getOrderIfExists(String orderNumber) {
        // Retrieve the order from the database or return null if not found
        return this.orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }

    /**
     * Retrieves a product by its product number if it exists.
     *
     * @param productNumber the product number to search for
     * @return the ProductDBO object if found, otherwise null
     */
    private ProductDBO getProductIfExists(String productNumber) {
        // Retrieve the product from the database or return null if not found
        return this.productRepository.findByProductNumber(productNumber).orElse(null);
    }

    /**
     * Retrieves an equipment by its equipment number if it exists.
     *
     * @param equipmentNumber the equipment number to search for
     * @return the EquipmentDBO object if found, otherwise null
     */
    private EquipmentDBO getEquipmentIfExists(String equipmentNumber) {
        // Retrieve the equipment from the database or return null if not found
        return this.equipmentRepository.findByEquipmentNumber(equipmentNumber).orElse(null);
    }

    /**
     * Sets the ID and nested IDs of the workflow to null.
     *
     * @param workflow the workflow to nullify IDs
     */
    private void nullifyWorkflowIds(EditorWorkflowTO workflow) {
        workflow.setId(null);
        for (EditorTaskTO task : workflow.getTasks()) {
            nullifyTaskIds(task);
        }
    }

    /**
     * Sets the ID and nested IDs of the task to null.
     *
     * @param task the task to nullify IDs
     */
    private void nullifyTaskIds(EditorTaskTO task) {
        task.setId(null);
        for (EditorItemTO item : task.getItems()) {
            nullifyItemIds(item);
        }
    }

    /**
     * Sets the ID of the item to null.
     *
     * @param item the item to nullify ID
     */
    private void nullifyItemIds(EditorItemTO item) {
        item.setId(null);
    }

}
