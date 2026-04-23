package com.enzobersano.booking_platform_api.auth.infrastructure.security;

import com.enzobersano.booking_platform_api.auth.application.port.UserRepositoryPort;
import com.enzobersano.booking_platform_api.auth.domain.valueobject.Email;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepositoryPort userRepository;

    public UserDetailsServiceImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String rawEmail) throws UsernameNotFoundException {
        var emailResult = Email.of(rawEmail);

        if (!emailResult.isSuccess()) {
            throw new UsernameNotFoundException("Malformed email: " + rawEmail);
        }

        return userRepository.findByEmail(emailResult.getValue())
                .map(user -> new User(
                        user.email().value(),
                        user.password().value(),
                        user.isActive(),
                        true, true, true,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.role().name()))
                ))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + rawEmail)
                );
    }
}