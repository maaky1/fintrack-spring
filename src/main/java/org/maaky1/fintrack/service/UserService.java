package org.maaky1.fintrack.service;

import org.maaky1.fintrack.entity.UserEntity;
import org.maaky1.fintrack.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }

    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public String getUsernameByUID(String UID) {
        UserEntity user = userRepository.findByUserId(UID);
        if (user == null)
            return null;

        return user.getUsername();
    }

    public boolean checkAvailUsername(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElse(null);

        return user == null;
    }

    public boolean checkAvailEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElse(null);

        return user == null;
    }

    public UserEntity saveUser(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}
