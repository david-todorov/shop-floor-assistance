package com.shopfloor.backend.services.interfaces;

import com.shopfloor.backend.api.transferobjects.operators.OperatorExecutionTO;
import com.shopfloor.backend.api.transferobjects.operators.OperatorForecastTO;
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

    OperatorOrderTO getOrder(Long id);

    OperatorExecutionTO startExecution(Long orderId);

    OperatorExecutionTO finishExecution(Long executionId);

    OperatorExecutionTO abortExecution(Long executionId);

    OperatorForecastTO getForecast(Long orderId);
}
