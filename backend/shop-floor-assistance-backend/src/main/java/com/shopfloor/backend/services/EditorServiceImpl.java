package com.shopfloor.backend.services;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.editors.EditorProductTO;
import com.shopfloor.backend.api.transferobjects.mappers.EditorTOMapper;
import com.shopfloor.backend.database.exceptions.*;
import com.shopfloor.backend.database.mappers.DBOInitializerMapper;
import com.shopfloor.backend.database.mappers.DBOUpdaterMapper;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.objects.ProductDBO;
import com.shopfloor.backend.database.repositories.OrderRepository;
import com.shopfloor.backend.database.repositories.ProductRepository;
import com.shopfloor.backend.database.repositories.UserRepository;
import com.shopfloor.backend.security.AuthenticatedUserDetails;
import com.shopfloor.backend.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    private final DBOInitializerMapper dboInitializerMapper;
    private final DBOUpdaterMapper dboUpdaterMapper;
    private final EditorTOMapper editorToMapper;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public EditorServiceImpl(DBOInitializerMapper dboInitializerMapper,
                             EditorTOMapper editorToMapper,
                             OrderRepository orderRepository,
                             JwtService jwtService,
                             UserRepository userRepository,
                             DBOUpdaterMapper dboUpdaterMapper,
                             ProductRepository productRepository) {
        this.dboInitializerMapper = dboInitializerMapper;
        this.dboUpdaterMapper = dboUpdaterMapper;
        this.editorToMapper = editorToMapper;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    //TODO
    @Override
    public List<EditorProductTO> getAllProducts() {
        return this.editorToMapper.toProductTOs(this.productRepository.findAll());
    }

    //TODO
    @Override
    public EditorProductTO addProduct(EditorProductTO newEditorProductTO) {
        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        if (getProductIfExists(newEditorProductTO.getProductNumber()) != null) {
            throw new DuplicateProductException();
        }

        ProductDBO newProductDBO = this.dboInitializerMapper.toProductDBO(newEditorProductTO, creatorId);

        return this.editorToMapper.toProductTO(this.productRepository.save(newProductDBO));
    }

    //TODO
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

    //TODO
    @Override
    public EditorProductTO getProduct(Long productId) {
        ProductDBO productDBO = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        return this.editorToMapper.toProductTO(productDBO);
    }

    //TODO
    @Override
    public void deleteProduct(Long productId) {
        ProductDBO toDeleteProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException());

        this.productRepository.delete(toDeleteProduct);
    }

    @Override
    public List<EditorOrderTO> getAllOrders() {
        return this.editorToMapper.toOrderTOs(this.orderRepository.findAll());
    }

    @Override
    public EditorOrderTO addOrder(EditorOrderTO newEditorOrderTO) {

        Long creatorId = AuthenticatedUserDetails.getCurrentUserId();

        if (getOrderIfExists(newEditorOrderTO.getOrderNumber()) != null) {
            throw new DuplicatedOrderException();
        }

        ProductDBO product = this.productRepository.findById(newEditorOrderTO.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException());

        OrderDBO newOrderDBO = this.dboInitializerMapper.toOrderDBO(newEditorOrderTO, creatorId);

        newOrderDBO.setProduct(product);

        return this.editorToMapper.toOrderTO(this.orderRepository.save(newOrderDBO));
    }

    @Transactional
    public EditorOrderTO updateOrder(Long orderId, EditorOrderTO updatedEditorOrderTO) {

        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        OrderDBO existingOrderDBO = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        ProductDBO product = this.productRepository.findById(updatedEditorOrderTO.getProduct().getId())
                .orElseThrow(() -> new ProductNotFoundException());

        if (orderRepository.existsByOrderNumberAndIdNot(updatedEditorOrderTO.getOrderNumber(), orderId)) {
            throw new DuplicatedOrderException();
        }

        this.dboUpdaterMapper.copyOrderDboFrom(existingOrderDBO, updatedEditorOrderTO, updaterId);

        return this.editorToMapper.toOrderTO(this.orderRepository.save(existingOrderDBO));
    }

    @Override
    public EditorOrderTO getOrder(Long orderId) {

        OrderDBO orderDBO = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

        return this.editorToMapper.toOrderTO(orderDBO);
    }

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

}
