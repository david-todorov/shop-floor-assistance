package com.shopfloor.backend.services.orders;

import com.shopfloor.backend.api.transferobjects.OrderTO;

import java.util.List;

/**
 * This is where all needed public methods should be declared
 * Think what EditorController would need and declare it here
 * Be generic not concrete
 * Rely on actions not implementations
 * Keep the number of methods low as possible and if
 * something does not feel right,
 * probably should be private and not here
 * Have fun
 */
public interface EditorService {

    List<OrderTO> getAllOrderAsTOs();
    OrderTO addOrderToDatabase(OrderTO newOrderTO, String authorizationHeader);
}
