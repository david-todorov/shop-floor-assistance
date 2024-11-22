package com.shopfloor.backend.api.transferobjects.editors;

import lombok.Getter;
import lombok.Setter;

/**
 * Transfer object for editor forecast.
 * Contains details about the forecast, including the total time required.
 * @author David Todorov (https://github.com/david-todorov)
 */
@Getter
@Setter
public class EditorForecastTO {
    /**
     * The total time required for the forecast.
     */
    private Integer totalTimeRequired;
}
