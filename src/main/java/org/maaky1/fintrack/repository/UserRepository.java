package org.maaky1.fintrack.repository;

import java.util.Optional;

import org.maaky1.fintrack.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUID(String UID);

    Optional<UserEntity> findByUsername(String username);
}
