package com.github.silviacristinaa.auth.security;

import com.github.silviacristinaa.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final UserRepository userRepository;

    public boolean isSelf(Long userId, Authentication authentication) {
        if (userId == null || authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return userRepository.findByLogin(authentication.getName())
                .map(user -> user.getUserId().equals(userId))
                .orElse(false);
    }
}
