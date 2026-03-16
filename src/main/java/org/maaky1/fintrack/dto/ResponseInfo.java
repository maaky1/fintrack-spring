package org.maaky1.fintrack.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseInfo<T> {
    private String status;
    private String code;
    private String message;
    private T data;
}
