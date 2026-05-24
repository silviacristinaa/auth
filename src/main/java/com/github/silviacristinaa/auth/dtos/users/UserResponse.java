package com.github.silviacristinaa.auth.dtos.users;

import java.util.Set;

public record UserResponse(
        Long userId,
        String login,
        String nome,
        String cpf,
        boolean enabled,
        Set<String> roles
) {
}
