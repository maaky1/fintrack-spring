package org.maaky1.fintrack.dto.request;

import lombok.Data;

@Data
public class RqRegister {
    private String name;
    private String username;
    private String email;
    private String password;
}
