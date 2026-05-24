package com.github.silviacristinaa.auth.services;

import com.github.silviacristinaa.auth.dtos.users.CreateUserRequest;
import com.github.silviacristinaa.auth.dtos.users.UserResponse;
import com.github.silviacristinaa.auth.entities.Role;
import com.github.silviacristinaa.auth.entities.User;
import com.github.silviacristinaa.auth.enums.RoleName;
import com.github.silviacristinaa.auth.exceptions.BusinessException;
import com.github.silviacristinaa.auth.exceptions.NotFoundException;
import com.github.silviacristinaa.auth.repositories.RoleRepository;
import com.github.silviacristinaa.auth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByLogin(request.login())) {
            throw new BusinessException("Login ja cadastrado");
        }

        if (userRepository.existsByCpf(request.cpf())) {
            throw new BusinessException("CPF ja cadastrado");
        }

        User user = new User();
        user.setLogin(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setNome(request.nome());
        user.setCpf(request.cpf());
        user.setEnabled(true);
        user.setRoles(resolveRoles(request.roles()));

        return toResponse(userRepository.save(user));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado"));
    }

    private Set<Role> resolveRoles(Set<String> roles) {
        return roles.stream()
                .map(this::parseRoleName)
                .map(roleName -> roleRepository.findByName(roleName)
                        .orElseThrow(() -> new BusinessException("Role nao cadastrada: " + roleName)))
                .collect(Collectors.toSet());
    }

    private RoleName parseRoleName(String role) {
        String normalizedRole = role.trim().toUpperCase();

        if ("FUNCIONARIO".equals(normalizedRole)) {
            normalizedRole = "EMPLOYEE";
        }

        if (!normalizedRole.startsWith("ROLE_")) {
            normalizedRole = "ROLE_" + normalizedRole;
        }

        try {
            return RoleName.valueOf(normalizedRole);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Role invalida: " + role);
        }
    }

    private UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getUserId(),
                user.getLogin(),
                user.getNome(),
                user.getCpf(),
                user.isEnabled(),
                roles);
    }
}
