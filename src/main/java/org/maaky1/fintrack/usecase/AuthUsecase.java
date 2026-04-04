package org.maaky1.fintrack.usecase;

import java.time.LocalDateTime;
import org.apache.logging.log4j.util.Strings;
import org.maaky1.fintrack.dto.RequestInfo;
import org.maaky1.fintrack.dto.ResponseInfo;
import org.maaky1.fintrack.dto.request.RqLogin;
import org.maaky1.fintrack.dto.request.RqRegister;
import org.maaky1.fintrack.dto.response.RsLogin;
import org.maaky1.fintrack.dto.response.RsRefreshToken;
import org.maaky1.fintrack.dto.response.RsRegister;
import org.maaky1.fintrack.entity.RefreshTokenEntity;
import org.maaky1.fintrack.entity.UserEntity;
import org.maaky1.fintrack.exception.AppException;
import org.maaky1.fintrack.service.RefreshTokenService;
import org.maaky1.fintrack.service.UserService;
import org.maaky1.fintrack.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.f4b6a3.ulid.UlidCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthUsecase {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    private static final String registerFuncCode = "11";
    private static final String refreshFuncCode = "13";

    public ResponseInfo<RsRegister> registerAccount(RequestInfo<RqRegister> request) {
        RqRegister rqRegister = request.getBody();
        if (rqRegister.getName() == null || rqRegister.getName().isBlank())
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(registerFuncCode, "01"), "Name is required");
        if (rqRegister.getUsername() == null || rqRegister.getUsername().isBlank())
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(registerFuncCode, "02"),
                    "Username is required");
        if (rqRegister.getEmail() == null || rqRegister.getEmail().isBlank())
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(registerFuncCode, "03"), "Email is required");
        if (rqRegister.getPassword() == null || !rqRegister.getPassword().matches("^[a-zA-Z0-9]{6,}$"))
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(registerFuncCode, "04"),
                    "Password min 6 characters and must be alphanumeric");

        boolean availUsername = userService.checkAvailUsername(rqRegister.getUsername());
        if (!availUsername)
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(registerFuncCode, "05"),
                    "Username is already registered");

        boolean checkAvailEmail = userService.checkAvailEmail(rqRegister.getEmail());
        if (!checkAvailEmail)
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(registerFuncCode, "05"),
                    "Email is already registered");

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

        return new ResponseInfo<RsRegister>()
                .setCode("00")
                .setStatus("Success")
                .setMessage("Register Success")
                .setData(rsRegister);
    }

    public ResponseInfo<RsLogin> doLogin(RequestInfo<RqLogin> request) {
        RqLogin rqLogin = request.getBody();
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(rqLogin.getUsername(), rqLogin.getPassword()));

        UserEntity user = userService.getUserByUsername(rqLogin.getUsername());

        String accessToken = jwtUtil.generateToken(user.getUserId());
        String refreshToken = generateRefreshToken(user);

        RsLogin rsLogin = new RsLogin()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);

        return new ResponseInfo<RsLogin>()
                .setCode("00")
                .setStatus("Success")
                .setMessage("Login Success")
                .setData(rsLogin);
    }

    public ResponseInfo<RsRefreshToken> refreshToken(RequestInfo<Void> request) {
        String refreshToken = request.getServletRequest().getHeader("X-Refresh-Token");
        RefreshTokenEntity data = refreshTokenService.getByRefreshToken(refreshToken);
        if (data == null)
            throw new AppException(HttpStatus.NOT_FOUND, Strings.concat(refreshFuncCode, "01"),
                    "Refresh token not found");

        if (data.isRevoked())
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(refreshFuncCode, "02"),
                    "Refresh token is revoked");

        if (data.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new AppException(HttpStatus.BAD_REQUEST, Strings.concat(refreshFuncCode, "03"),
                    "Refresh token expired");

        String newAccessToken = jwtUtil.generateToken(data.getUser().getUserId());
        RsRefreshToken rsRefreshToken = new RsRefreshToken()
                .setAccessToken(newAccessToken);

        return new ResponseInfo<RsRefreshToken>()
                .setCode("00")
                .setStatus("Success")
                .setMessage("Refresh Token Success")
                .setData(rsRefreshToken);
    }

    private String generateRefreshToken(UserEntity user) {
        String refreshToken = Strings.concat("RT-", UlidCreator.getMonotonicUlid().toString());

        RefreshTokenEntity refreshTokenEntity = refreshTokenService.getByUser(user);
        if (refreshTokenEntity == null)
            refreshTokenEntity = new RefreshTokenEntity().setUser(user);

        refreshTokenEntity.setUser(user)
                .setToken(refreshToken)
                .setExpiresAt(LocalDateTime.now().plusDays(7));

        refreshTokenService.saveRefreshToken(refreshTokenEntity);

        return refreshToken;
    }
}
