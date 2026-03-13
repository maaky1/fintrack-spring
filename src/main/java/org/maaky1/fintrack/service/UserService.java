package org.maaky1.fintrack.service;

import org.maaky1.fintrack.entity.UserEntity;
import org.maaky1.fintrack.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void getByUsername(String username) {
        UserEntity result = userRepository.findByUsername(username);
        log.info("{}", result);
    }
}
