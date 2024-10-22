package com.shopfloor.backend.services;

import com.shopfloor.backend.api.transferobjects.editors.EditorOrderTO;
import com.shopfloor.backend.api.transferobjects.mappers.EditorTOMapper;
import com.shopfloor.backend.database.exceptions.*;
import com.shopfloor.backend.database.mappers.DBOInitializerMapper;
import com.shopfloor.backend.database.mappers.DBOUpdaterMapper;
import com.shopfloor.backend.database.objects.OrderDBO;
import com.shopfloor.backend.database.repositories.OrderRepository;
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
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public EditorServiceImpl(DBOInitializerMapper dboInitializerMapper,
                             EditorTOMapper editorToMapper,
                             OrderRepository orderRepository,
                             JwtService jwtService,
                             UserRepository userRepository,
                             DBOUpdaterMapper dboUpdaterMapper) {
        this.dboInitializerMapper = dboInitializerMapper;
        this.dboUpdaterMapper = dboUpdaterMapper;
        this.editorToMapper = editorToMapper;
        this.orderRepository = orderRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
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

        OrderDBO newOrderDBO = this.dboInitializerMapper.toOrderDBO(newEditorOrderTO, creatorId);

        return this.editorToMapper.toOrderTO(this.orderRepository.save(newOrderDBO));
    }

    @Transactional
    public EditorOrderTO updateOrder(Long orderId, EditorOrderTO updatedEditorOrderTO) {

        Long updaterId = AuthenticatedUserDetails.getCurrentUserId();

        OrderDBO existingOrderDBO = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException());

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

}
