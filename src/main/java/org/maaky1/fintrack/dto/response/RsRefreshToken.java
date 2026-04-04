package org.maaky1.fintrack.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RsRefreshToken {
    private String accessToken;
}
