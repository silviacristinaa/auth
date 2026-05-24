package com.github.silviacristinaa.auth.dtos.users;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CreateUserRequest(
        @NotBlank String login,
        @NotBlank String password,
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotEmpty Set<String> roles
) {
}
