package com.shopfloor.backend.api.transferobjects.operators;

import lombok.Getter;
import lombok.Setter;

/**
 * Transfer object for operator forecast.
 * Contains details about the forecast, including the total time required.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class OperatorForecastTO {
    /**
     * The total time required for the operator forecast.
     */
    private Integer totalTimeRequired;
}
