package org.maaky1.fintrack.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import lombok.extern.slf4j.Slf4j;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class AppPropertiesTest {
    @Autowired
    private AppProperties appProperties;

    @Test
    void getAppProperties() {
        assertThat(appProperties.getJWT_SECRET_KEY()).isEqualTo("aVG6sHEdToZ+3xau2ocOXKXfDVhxpU5EWxw/NG7/AcM=");
    }
}
