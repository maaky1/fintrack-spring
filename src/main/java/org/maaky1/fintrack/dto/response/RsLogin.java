package org.maaky1.fintrack.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RsLogin {
    private String accessToken;
    private String refreshToken;
}
