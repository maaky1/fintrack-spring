package org.maaky1.fintrack.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class RequestInfo<T> {
    private String requestId;
    private String operationName;
    private T body;
}