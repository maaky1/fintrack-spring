package org.maaky1.fintrack.util;

import java.util.UUID;
import org.maaky1.fintrack.dto.RequestInfo;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {
    public static <T> RequestInfo<T> constructRequest(
            String operationName,
            T body,
            HttpServletRequest servletRequest) {

        return RequestInfo.<T>builder()
                .requestId(UUID.randomUUID().toString())
                .operationName(operationName)
                .body(body)
                .servletRequest(servletRequest)
                .build();
    }
}
