package org.maaky1.fintrack.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.maaky1.fintrack.configuration.AppProperties;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.f4b6a3.ulid.UlidCreator;

import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {
    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        when(appProperties.getJWT_SECRET_KEY()).thenReturn("aVG6sHEdToZ+3xau2ocOXKXfDVhxpU5EWxw/NG7/AcM=");
    }

    @Test
    void testGenerateToken() {
        when(appProperties.getAPP_NAME()).thenReturn("fintrack");
        when(appProperties.getJWT_EXPIRATION()).thenReturn(900000L);

        String UID = UlidCreator.getUlid().toString();
        log.info("UID: {}", UID);
        assertThat(UID).isNotNull();
        String token = jwtUtil.generateToken(UID);
        log.info("Token: {}", token);
        assertThat(token).isNotNull();
    }

    @Test
    void testIsValidToken() {
        boolean validToken = jwtUtil.isValidToken(
                "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmaW50cmFjayIsImlhdCI6MTc3MzM3NDcyMCwiZXhwIjoxNzczMzc1NjIwLCJzdWIiOiIwMUtLSk5YSkZHMjE1NjU1NzYyUU5ORUM5MCJ9.7Dalzt-Ja4n9TfZvPsF1CznCJeEIIxexfO_cEjKwtdk");
        log.info("Valid: {}", validToken);
        assertThat(validToken).isTrue();
    }

    @Test
    void testExtractUID() {
        String UID = jwtUtil.extractUID(
                "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmaW50cmFjayIsImlhdCI6MTc3MzM3NDcyMCwiZXhwIjoxNzczMzc1NjIwLCJzdWIiOiIwMUtLSk5YSkZHMjE1NjU1NzYyUU5ORUM5MCJ9.7Dalzt-Ja4n9TfZvPsF1CznCJeEIIxexfO_cEjKwtdk");
        log.info("UID: {}", UID);
        assertThat(UID).isNotNull();
    }
}
