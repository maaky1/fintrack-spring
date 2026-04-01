package org.maaky1.fintrack.dto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestInfo<T> {
    private String requestId;
    private String operationName;
    private T body;
    private HttpServletRequest servletRequest;
}