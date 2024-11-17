package com.shopfloor.backend.services.implementations;

import com.shopfloor.backend.api.transferobjects.editors.*;
import com.shopfloor.backend.api.transferobjects.mappers.EditorTOMapper;
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
 * This is where the concrete implementations of EditorService is
 * Of course implement all the needed methods
 * But do not be afraid to extract common logic in private methods
 * The number of public methods should be the same as in EditorService
 * If you implement something public here and NOT declare it in EditorService
 * The controller EditorController can NOT see it
 * Have fun
 */
@Component
public class EditorServiceImpl implements EditorService {

    private final OrderDBOMapper orderDBOMapper;
    private final ProductDBOMapper productDBOMapper;
    private final EquipmentDBOMapper equipmentDBOMapper;

    private final EditorTOMapper editorToMapper;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EquipmentRepository equipmentRepository;

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

    @Override
    public List<EditorEquipmentTO> getEquipmentSuggestions(int numberOfEquipments) {
        Pageable pageable = PageRequest.of(0, numberOfEquipments);

        return this.editorToMapper.toEquipmentTOs(this.equipmentRepository.findTopReferencedEquipment(pageable));
    }

    @Override
    public List<EditorProductTO> getProductsSuggestions(int numberOfProducts) {
        Pageable pageable = PageRequest.of(0, numberOfProducts);

        return this.editorToMapper.toProductTOs(this.productRepository.findTopReferencedProducts(pageable));
    }

    @Override
    public List<EditorWorkflowTO> getWorkflowsSuggestions(EditorProductTO productAfter) {
        List<EditorWorkflowTO> suggested = new ArrayList<>();
        Long productId = productAfter.getId();

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

    @Override
    public List<EditorTaskTO> getTasksSuggestions(EditorProductTO productAfter) {
        List<EditorTaskTO> suggested = new ArrayList<>();
        Long productId = productAfter.getId();

        ProductDBO productAfterDBO = this.productRepository.findById(productId).orElse(null);
        if (productAfterDBO == null) {
            throw new ProductNotFoundException();
        }

        // Use streams to filter orders where 'productBefore' is null
        productAfterDBO.getOrdersAsAfterProduct().stream()
                .filter(ordersAsAfter -> ordersAsAfter.getBeforeProduct() == null)
                .forEach(ordersAsAfter -> ordersAsAfter.getWorkflows().forEach(
                        workflow -> this.editorToMapper.toTaskTOs(workflow.getTasks())
                                .forEach(task -> {
                                    nullifyTaskIds(task); // Set ID and nested IDs to null
                                    suggested.add(task);
                                })
                ));

        return suggested;
    }

    @Override
    public List<EditorItemTO> getItemsSuggestions(EditorProductTO productAfter) {
        List<EditorItemTO> suggested = new ArrayList<>();
        Long productId = productAfter.getId();

        ProductDBO productAfterDBO = this.productRepository.findById(productId).orElse(null);
        if (productAfterDBO == null) {
            throw new ProductNotFoundException();
        }

        // Use streams to filter out orders where 'productBefore' is null
        productAfterDBO.getOrdersAsAfterProduct().stream()
                .filter(ordersAsAfter -> ordersAsAfter.getBeforeProduct() == null)
                .forEach(ordersAsAfter -> ordersAsAfter.getWorkflows().forEach(
                        workflow -> workflow.getTasks().forEach(
                                task -> this.editorToMapper.toItemTOs(task.getItems())
                                        .forEach(item -> {
                                            nullifyItemIds(item); // Set ID to null
                                            suggested.add(item);
                                        })
                        )
                ));

        return suggested;
    }

    @Override
    public List<EditorEquipmentTO> getAllEquipment() {
        return this.editorToMapper.toEquipmentTOs(this.equipmentRepository.findAll());
    }

    @Override
    public EditorEquipmentTO getEquipment(Long equipmentId) {
        EquipmentDBO equipmentDBO = this.equipmentRepository.findById(equipmentId).orElseThrow(EquipmentNotFoundException::new);

        return this.editorToMapper.toEquipmentTO(equipmentDBO);
    }

    @Transactional
    @Override
    public EditorEquipmentTO addEquipment(EditorEquipmentTO newEditorEquipmentTO) {
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        if (this.getEquipmentIfExists(newEditorEquipmentTO.getEquipmentNumber()) != null) {
            throw new DuplicateEquipmentException();
        }

        EquipmentDBO equipmentDBO = this.equipmentDBOMapper.initializeEquipmentDBOFrom(newEditorEquipmentTO, creatorId);

        return this.editorToMapper.toEquipmentTO(this.equipmentRepository.save(equipmentDBO));
    }

    @Transactional
    @Override
    public EditorEquipmentTO updateEquipment(Long equipmentId, EditorEquipmentTO updatedEditorEquipmentTO) {

        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        EquipmentDBO existingEquipmentDBO = this.equipmentRepository.findById(equipmentId).orElseThrow(EquipmentNotFoundException::new);

        if (this.equipmentRepository.existsByEquipmentNumberAndIdNot(updatedEditorEquipmentTO.getEquipmentNumber(), equipmentId)) {
            throw new DuplicateEquipmentException();
        }

        this.equipmentDBOMapper.updateEquipmentDBOFrom(existingEquipmentDBO, updatedEditorEquipmentTO, updaterId);

        return this.editorToMapper.toEquipmentTO(this.equipmentRepository.save(existingEquipmentDBO));
    }

    @Transactional
    @Override
    public void deleteEquipment(Long equipmentId) {
        EquipmentDBO equipmentDBO = this.equipmentRepository.findById(equipmentId).orElseThrow(EquipmentNotFoundException::new);

        // Clear order references to the equipment
        equipmentDBO.clearOrderReferences();

        this.equipmentRepository.delete(equipmentDBO);
    }

    @Override
    public List<EditorProductTO> getAllProducts() {
        return this.editorToMapper.toProductTOs(this.productRepository.findAll());
    }

    @Override
    public EditorProductTO getProduct(Long productId) {
        ProductDBO productDBO = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        return this.editorToMapper.toProductTO(productDBO);
    }

    @Transactional
    @Override
    public EditorProductTO addProduct(EditorProductTO newEditorProductTO) {
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        if (getProductIfExists(newEditorProductTO.getProductNumber()) != null) {
            throw new DuplicateProductException();
        }

        ProductDBO newProductDBO = this.productDBOMapper.initializeProductDBO(newEditorProductTO, creatorId);

        return this.editorToMapper.toProductTO(this.productRepository.save(newProductDBO));
    }

    @Transactional
    @Override
    public EditorProductTO updateProduct(Long productId, EditorProductTO editorProductTO) {

        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        ProductDBO existingProductDBO = this.productRepository.findById(editorProductTO.getId()).orElseThrow(ProductNotFoundException::new);

        if (productRepository.existsByProductNumberAndIdNot(editorProductTO.getProductNumber(), productId)) {
            throw new DuplicateProductException();
        }

        this.productDBOMapper.updateProductDboFrom(existingProductDBO, editorProductTO, updaterId);

        return this.editorToMapper.toProductTO(this.productRepository.save(existingProductDBO));
    }

    @Transactional
    @Override
    public void deleteProduct(Long productId) {
        ProductDBO toDeleteProduct = productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);

        // Clear order references to the product
        toDeleteProduct.clearOrderReferences();

        this.productRepository.delete(toDeleteProduct);
    }

    @Override
    public List<EditorOrderTO> getAllOrders() {
        return this.editorToMapper.toOrderTOs(this.orderRepository.findAll());
    }

    @Override
    public EditorOrderTO getOrder(Long orderId) {

        OrderDBO orderDBO = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        return this.editorToMapper.toOrderTO(orderDBO);
    }

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

        // Get the new equipment list and updates it
        List<EquipmentDBO> equipmentList = updatedEditorOrderTO.getEquipment().stream()
                .map(equipmentTO -> this.equipmentRepository.findById(equipmentTO.getId())
                        .orElseThrow(EquipmentNotFoundException::new))
                .toList();
        existingOrderDBO.synchronizeEquipmentList(equipmentList);

        // Save the updated order
        existingOrderDBO = this.orderDBOMapper.updateOrderDBOFrom(existingOrderDBO, updatedEditorOrderTO, updaterId);
        existingOrderDBO = this.orderRepository.save(existingOrderDBO);

        //Sorting entities according to the ordering index, which comes directly from the order of the JSON request
        existingOrderDBO.sortEntities();

        // Convert the updated OrderDBO back to EditorOrderTO to return to the frontend
        return this.editorToMapper.toOrderTO(existingOrderDBO);
    }

    @Transactional
    @Override
    public void deleteOrder(Long orderId) {
        // Retrieve the order to be deleted
        OrderDBO existingOrderDBO = orderRepository.findById(orderId).orElseThrow(OrderNotFoundException::new);

        // Use helper methods to maintain bidirectional relationships
        existingOrderDBO.clearBeforeProduct();
        existingOrderDBO.clearAfterProduct();

        // Clear equipment references using the helper method
        existingOrderDBO.clearEquipmentList();

        // Clear Execution references using helper method
        existingOrderDBO.clearExecutions();

        // Finally, delete the order itself from the repository
        orderRepository.delete(existingOrderDBO);
    }

    private OrderDBO getOrderIfExists(String orderNumber) {
        return this.orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }

    private ProductDBO getProductIfExists(String productNumber) {
        return this.productRepository.findByProductNumber(productNumber).orElse(null);
    }

    private EquipmentDBO getEquipmentIfExists(String equipmentNumber) {
        return this.equipmentRepository.findByEquipmentNumber(equipmentNumber).orElse(null);
    }

    private void nullifyWorkflowIds(EditorWorkflowTO workflow) {
        workflow.setId(null);
        for (EditorTaskTO task : workflow.getTasks()) {
            nullifyTaskIds(task);
        }
    }

    private void nullifyTaskIds(EditorTaskTO task) {
        task.setId(null);
        for (EditorItemTO item : task.getItems()) {
            nullifyItemIds(item);
        }
    }

    private void nullifyItemIds(EditorItemTO item) {
        item.setId(null);
    }

}