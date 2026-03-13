package org.maaky1.fintrack.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String APP_NAME;
    private String JWT_SECRET_KEY;
    private long JWT_EXPIRATION;
}
