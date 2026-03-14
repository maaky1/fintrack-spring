package org.maaky1.fintrack.configuration.security;

import java.io.IOException;

import org.maaky1.fintrack.repository.UserRepository;
import org.maaky1.fintrack.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private static final String prefixAuth = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(prefixAuth)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(prefixAuth.length());
        if (jwtUtil.isValidToken(token)) {
            String UID = jwtUtil.extractUID(token);

        }
    }

}
