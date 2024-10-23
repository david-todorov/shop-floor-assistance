package com.shopfloor.backend.services;

import com.shopfloor.backend.api.transferobjects.operators.OperatorOrderTO;

import java.util.List;

/**
 * This is where all needed public methods should be declared
 * Think what OperatorController would need and declare it here
 * Be generic not concrete
 * Rely on actions not implementations
 * Keep the number of methods low as possible and if
 * something does not feel right,
 * probably should be private and not here
 * Have fun
 */
public interface OperatorService {

    List<OperatorOrderTO> getAllOrders();

    OperatorOrderTO getOrder(long id);
}
