package com.github.silviacristinaa.auth.dtos.auth;

import java.util.List;

public record ValidateTokenResponse(
        boolean authenticated,
        boolean expired,
        String login,
        List<String> roles
) {
}
