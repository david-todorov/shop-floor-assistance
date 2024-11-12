package com.shopfloor.backend.api.transferobjects.operators;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class OperatorExecutionTO {
    private Long id;
    private Date startedAt;
    private Date finishedAt;
    private Boolean aborted;
    private Long startedBy;
    private Long finishedBy;
}
