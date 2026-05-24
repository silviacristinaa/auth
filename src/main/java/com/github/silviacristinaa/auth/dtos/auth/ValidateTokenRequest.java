package com.github.silviacristinaa.auth.dtos.auth;

import jakarta.validation.constraints.NotBlank;

public record ValidateTokenRequest(
        @NotBlank String token
) {
}
