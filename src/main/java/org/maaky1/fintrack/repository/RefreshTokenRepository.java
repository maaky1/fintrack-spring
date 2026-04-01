package org.maaky1.fintrack.repository;

import java.util.Optional;

import org.maaky1.fintrack.entity.RefreshTokenEntity;
import org.maaky1.fintrack.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUser(UserEntity user);

    Optional<RefreshTokenEntity> findByRefreRefreshToken(String refreshToken);
}
