package org.maaky1.fintrack.controller;

import org.maaky1.fintrack.dto.RequestInfo;
import org.maaky1.fintrack.dto.ResponseInfo;
import org.maaky1.fintrack.dto.request.RqLogin;
import org.maaky1.fintrack.dto.request.RqRegister;
import org.maaky1.fintrack.dto.response.RsLogin;
import org.maaky1.fintrack.dto.response.RsRefreshToken;
import org.maaky1.fintrack.dto.response.RsRegister;
import org.maaky1.fintrack.usecase.AuthUsecase;
import org.maaky1.fintrack.util.CommonUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final AuthUsecase authUsecase;

    @PostMapping("/auth/register")
    public ResponseEntity<ResponseInfo<RsRegister>> registerAccount(@RequestBody RqRegister body,
            HttpServletRequest httpServletRequest) {
        RequestInfo<RqRegister> requestInfo = CommonUtil.constructRequest("register-account", body, httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(authUsecase.registerAccount(requestInfo));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseInfo<RsLogin>> doLogin(@RequestBody RqLogin body,
            HttpServletRequest httpServletRequest) {
        RequestInfo<RqLogin> requestInfo = CommonUtil.constructRequest("do-login", body, httpServletRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authUsecase.doLogin(requestInfo));
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<ResponseInfo<RsRefreshToken>> refreshToken(
            @RequestHeader("X-Refresh-Token") String refreshToken, HttpServletRequest httpServletRequest) {
        RequestInfo<Void> requestInfo = CommonUtil.constructRequest("refresh-token", null, httpServletRequest);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authUsecase.refreshToken(requestInfo));
    }
}
