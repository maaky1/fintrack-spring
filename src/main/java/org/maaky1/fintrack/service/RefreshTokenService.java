package org.maaky1.fintrack.service;

import org.maaky1.fintrack.entity.RefreshTokenEntity;
import org.maaky1.fintrack.entity.UserEntity;
import org.maaky1.fintrack.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenEntity saveRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    public RefreshTokenEntity getByUser(UserEntity user) {
        return refreshTokenRepository.findByUser(user).orElse(null);
    }

    public RefreshTokenEntity getByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken).orElse(null);
    }
}
