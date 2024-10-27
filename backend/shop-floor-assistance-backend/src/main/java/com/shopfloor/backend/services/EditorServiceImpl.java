package com.shopfloor.backend.services;

import com.shopfloor.backend.api.transferobjects.editors.EditorEquipmentTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import com.shopfloor.backend.api.transferobjects.mappers.EditorTOMapper;
import com.shopfloor.backend.database.exceptions.*;
import com.shopfloor.backend.database.mappers.DBOInitializerMapper;
import com.shopfloor.backend.database.mappers.DBOUpdaterMapper;
import com.shopfloor.backend.database.objects.EquipmentDBO;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.ProductDBO;
import com.shopfloor.backend.database.repositories.EquipmentRepository;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.database.repositories.ProductRepository;
import com.shopfloor.backend.security.AuthenticatedUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private final DBOInitializerMapper dboInitializerMapper;
    private final DBOUpdaterMapper dboUpdaterMapper;
    private final EditorTOMapper editorToMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EditorServiceImpl(DBOInitializerMapper dboInitializerMapper,
                             EditorTOMapper editorToMapper,
                             OrderRepository orderRepository,
                             DBOUpdaterMapper dboUpdaterMapper,
                             ProductRepository productRepository,
                             EquipmentRepository equipmentRepository) {
        this.dboInitializerMapper = dboInitializerMapper;
        this.dboUpdaterMapper = dboUpdaterMapper;
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
    public List<EditorEquipmentTO> getAllEquipment() {
        return this.editorToMapper.toEquipmentTOs(this.equipmentRepository.findAll());
    }

    @Transactional
    @Override
    public EditorEquipmentTO addEquipment(EditorEquipmentTO newEditorEquipmentTO) {
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        if (this.getEquipmentIfExists(newEditorEquipmentTO.getEquipmentNumber()) != null) {
            throw new DuplicateEquipmentException();
        }

        EquipmentDBO equipmentDBO = this.dboInitializerMapper.toEquipmentDBO(newEditorEquipmentTO, creatorId);

        return this.editorToMapper.toEquipmentTO(this.equipmentRepository.save(equipmentDBO));
    }

    @Transactional
    @Override
    public EditorEquipmentTO updateEquipment(Long equipmentId, EditorEquipmentTO updatedEditorEquipmentTO) {

        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        EquipmentDBO existingEquipmentDBO = this.equipmentRepository.findById(equipmentId).orElseThrow(() -> new EquipmentNotFoundException());

        if (this.equipmentRepository.existsByEquipmentNumberAndIdNot(updatedEditorEquipmentTO.getEquipmentNumber(), equipmentId)) {
            throw new DuplicateEquipmentException();
        }

        this.dboUpdaterMapper.copyEquipmentDboFrom(existingEquipmentDBO, updatedEditorEquipmentTO, updaterId);

        return this.editorToMapper.toEquipmentTO(this.equipmentRepository.save(existingEquipmentDBO));
    }

    @Override
    public EditorEquipmentTO getEquipment(Long equipmentId) {
        EquipmentDBO equipmentDBO = this.equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentNotFoundException());

        return this.editorToMapper.toEquipmentTO(equipmentDBO);
    }

    @Transactional
    @Override
    public void deleteEquipment(Long equipmentId) {
        EquipmentDBO equipmentDBO = this.equipmentRepository.findById(equipmentId)
                .orElseThrow(() -> new EquipmentNotFoundException());

        this.equipmentRepository.delete(equipmentDBO);
    }

    @Override
    public EditorProductTO getProduct(Long productId) {
        ProductDBO productDBO = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        return this.editorToMapper.toProductTO(productDBO);
    }

    @Transactional
    @Override
    public void deleteProduct(Long productId) {
        ProductDBO toDeleteProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        this.productRepository.delete(toDeleteProduct);
    }

    @Override
    public List<EditorProductTO> getAllProducts() {
        return this.editorToMapper.toProductTOs(this.productRepository.findAll());
    }

    @Transactional
    @Override
    public EditorProductTO addProduct(EditorProductTO newEditorProductTO) {
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        if (getProductIfExists(newEditorProductTO.getProductNumber()) != null) {
            throw new DuplicateProductException();
        }

        ProductDBO newProductDBO = this.dboInitializerMapper.toProductDBO(newEditorProductTO, creatorId);

        return this.editorToMapper.toProductTO(this.productRepository.save(newProductDBO));
    }

    @Transactional
    @Override
    public EditorProductTO updateProduct(Long productId, EditorProductTO editorProductTO) {

        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        ProductDBO existingProductDBO = this.productRepository.findById(editorProductTO.getId()).orElseThrow(() -> new ProductNotFoundException());

        if (productRepository.existsByProductNumberAndIdNot(editorProductTO.getProductNumber(), productId)) {
            throw new DuplicateProductException();
        }

        this.dboUpdaterMapper.copyProductDboFrom(existingProductDBO, editorProductTO, updaterId);

        return this.editorToMapper.toProductTO(this.productRepository.save(existingProductDBO));
    }

    @Override
    public List<EditorOrderTO> getAllOrders() {
        return this.editorToMapper.toOrderTOs(this.orderRepository.findAll());
    }

    @Transactional
    @Override
    public EditorOrderTO addOrder(EditorOrderTO newEditorOrderTO) {

        //The id of the user
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        //Checking if another order exists with the same order number
        if (getOrderIfExists(newEditorOrderTO.getOrderNumber()) != null) {
            throw new DuplicatedOrderException();
        }

        //Setting the basic properties of the order
        OrderDBO newOrderDBO = this.dboInitializerMapper.toOrderDBO(newEditorOrderTO, creatorId);

        //Fetching and setting the associated product
        ProductDBO product = this.productRepository.findById(newEditorOrderTO.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException());
        newOrderDBO.setProduct(product);

        // Fetch and setting associated equipment with the order
        List<EquipmentDBO> equipmentList = new ArrayList<>();
        for (EditorEquipmentTO equipmentTO : newEditorOrderTO.getEquipment()) {
            EquipmentDBO equipment = this.equipmentRepository.findById(equipmentTO.getId())
                    .orElseThrow(() -> new EquipmentNotFoundException());
            equipmentList.add(equipment);
        }
        newOrderDBO.setEquipment(equipmentList);

        //Saving and returning the added order
        return this.editorToMapper.toOrderTO(this.orderRepository.save(newOrderDBO));
    }

    @Transactional
    @Override
    public EditorOrderTO updateOrder(Long orderId, EditorOrderTO updatedEditorOrderTO) {

        //The id of the user
        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        //Checking if the order exists
        OrderDBO existingOrderDBO = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        // Check for duplicate order numbers
        if (orderRepository.existsByOrderNumberAndIdNot(updatedEditorOrderTO.getOrderNumber(), orderId)) {
            throw new DuplicatedOrderException();
        }

        //Updating the basic properties of the order
        dboUpdaterMapper.copyOrderDboFrom(existingOrderDBO, updatedEditorOrderTO, updaterId);

        // Update and set the product for the order
        ProductDBO product = productRepository.findById(updatedEditorOrderTO.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException());
        existingOrderDBO.setProduct(product);

        // Update and set the associated equipment list
        List<EquipmentDBO> newEquipments = updatedEditorOrderTO.getEquipment().stream()
                .map(equipmentTO -> equipmentRepository.findById(equipmentTO.getId())
                        .orElseThrow(() -> new EquipmentNotFoundException()))
                .collect(Collectors.toList());
        existingOrderDBO.getEquipment().clear();
        existingOrderDBO.getEquipment().addAll(newEquipments);

        //Saving and returning the updated order
        return editorToMapper.toOrderTO(orderRepository.save(existingOrderDBO));
    }

    @Override
    public EditorOrderTO getOrder(Long orderId) {

        OrderDBO orderDBO = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        return this.editorToMapper.toOrderTO(orderDBO);
    }

    @Transactional
    @Override
    public void deleteOrder(Long orderId) {

        OrderDBO toDeleteOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        this.orderRepository.delete(toDeleteOrder);
    }

    private OrderDBO getOrderIfExists(String orderNumber) {
        return this.orderRepository.findByOrderNumber(orderNumber).orElse(null);
    }

    private ProductDBO getProductIfExists(String orderNumber) {
        return this.productRepository.findByProductNumber(orderNumber).orElse(null);
    }

    private EquipmentDBO getEquipmentIfExists(String equipmentNumber) {
        return this.equipmentRepository.findByEquipmentNumber(equipmentNumber).orElse(null);
    }

}
