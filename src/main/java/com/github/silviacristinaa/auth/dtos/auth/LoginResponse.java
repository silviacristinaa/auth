package com.github.silviacristinaa.auth.dtos.auth;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresIn
) {
}
