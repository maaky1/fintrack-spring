package org.maaky1.fintrack.controller;

import java.util.UUID;

import org.maaky1.fintrack.dto.RequestInfo;
import org.maaky1.fintrack.dto.ResponseInfo;
import org.maaky1.fintrack.dto.request.RqLogin;
import org.maaky1.fintrack.dto.request.RqRegister;
import org.maaky1.fintrack.dto.response.RsLogin;
import org.maaky1.fintrack.dto.response.RsRegister;
import org.maaky1.fintrack.usecase.AuthUsecase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUsecase authUsecase;

    @PostMapping("/auth/register")
    public ResponseInfo<RsRegister> registerAccount(@RequestBody RqRegister body) {
        RequestInfo<RqRegister> requestInfo = RequestInfo.<RqRegister>builder()
                .requestId(UUID.randomUUID().toString())
                .operationName("register-account")
                .body(body)
                .build();

        ResponseInfo<RsRegister> response = authUsecase.registerAccount(requestInfo);
        return response;
    }

    @PostMapping("/auth/login")
    public ResponseInfo<RsLogin> doLogin(@RequestBody RqLogin body) {
        RequestInfo<RqLogin> requestInfo = RequestInfo.<RqLogin>builder()
                .requestId(UUID.randomUUID().toString())
                .operationName("do-login")
                .body(body)
                .build();

        ResponseInfo<RsLogin> response = authUsecase.doLogin(requestInfo);
        return response;
    }
}
