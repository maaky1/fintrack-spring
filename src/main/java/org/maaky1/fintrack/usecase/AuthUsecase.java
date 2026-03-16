package org.maaky1.fintrack.usecase;

import java.time.LocalDateTime;

import org.apache.logging.log4j.util.Strings;
import org.maaky1.fintrack.dto.RequestInfo;
import org.maaky1.fintrack.dto.ResponseInfo;
import org.maaky1.fintrack.dto.request.RqLogin;
import org.maaky1.fintrack.dto.request.RqRegister;
import org.maaky1.fintrack.dto.response.RsLogin;
import org.maaky1.fintrack.dto.response.RsRegister;
import org.maaky1.fintrack.entity.RefreshTokenEntity;
import org.maaky1.fintrack.entity.UserEntity;
import org.maaky1.fintrack.service.RefreshTokenService;
import org.maaky1.fintrack.service.UserService;
import org.maaky1.fintrack.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.f4b6a3.ulid.UlidCreator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUsecase {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public ResponseInfo<RsRegister> registerAccount(RequestInfo<RqRegister> request) {
        ResponseInfo<RsRegister> response = new ResponseInfo<RsRegister>()
                .setCode("00")
                .setStatus("Success");

        RqRegister rqRegister = request.getBody();
        if (rqRegister.getName() == null || rqRegister.getName().isBlank())
            return response.setCode("01").setStatus("Failed").setMessage("Name is required");
        if (rqRegister.getUsername() == null || rqRegister.getUsername().isBlank())
            return response.setCode("02").setStatus("Failed").setMessage("Username is required");
        if (rqRegister.getEmail() == null || rqRegister.getEmail().isBlank())
            return response.setCode("03").setStatus("Failed").setMessage("Email is required");
        if (rqRegister.getPassword() == null || !rqRegister.getPassword().matches("^[a-zA-Z0-9]{6,}$"))
            return response.setCode("04").setStatus("Failed")
                    .setMessage("Password min 6 characters and must be alphanumeric");

        boolean availUsername = userService.checkAvailUsername(rqRegister.getUsername());
        if (!availUsername)
            return response.setCode("05").setStatus("Failed").setMessage("Username is already registered");

        boolean checkAvailEmail = userService.checkAvailEmail(rqRegister.getEmail());
        if (!checkAvailEmail)
            return response.setCode("05").setStatus("Failed").setMessage("Email is already registered");

        String UID = UlidCreator.getMonotonicUlid().toString();
        UserEntity userEntity = new UserEntity()
                .setUserId(UID)
                .setName(rqRegister.getName())
                .setUsername(rqRegister.getUsername())
                .setEmail(rqRegister.getEmail())
                .setPassword(passwordEncoder.encode(rqRegister.getPassword()));
        UserEntity saveUser = userService.saveUser(userEntity);

        RsRegister rsRegister = new RsRegister()
                .setUserId(saveUser.getUserId())
                .setUsername(saveUser.getUsername());

        return response.setMessage("Register Success").setData(rsRegister);
    }

    public ResponseInfo<RsLogin> doLogin(RequestInfo<RqLogin> request) {
        ResponseInfo<RsLogin> response = new ResponseInfo<RsLogin>()
                .setCode("00")
                .setStatus("Success");

        RqLogin rqLogin = request.getBody();
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(rqLogin.getUsername(), rqLogin.getPassword()));

        UserEntity user = userService.getUserByUsername(rqLogin.getUsername());

        String accessToken = jwtUtil.generateToken(user.getUserId());
        String refreshToken = generateRefreshToken(user);

        RsLogin rsLogin = new RsLogin()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);

        return response.setMessage("Login Success").setData(rsLogin);
    }

    private String generateRefreshToken(UserEntity user) {
        String refreshToken = Strings.concat("RT-", UlidCreator.getMonotonicUlid().toString());

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.findByUser(user);
        if (refreshTokenEntity == null)
            refreshTokenEntity = new RefreshTokenEntity().setUser(user);

        refreshTokenEntity.setUser(user)
                .setToken(refreshToken)
                .setExpiresAt(LocalDateTime.now().plusDays(7));

        refreshTokenService.saveRefreshToken(refreshTokenEntity);

        return refreshToken;
    }
}
