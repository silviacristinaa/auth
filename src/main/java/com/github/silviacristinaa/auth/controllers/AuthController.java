package com.github.silviacristinaa.auth.controllers;

import com.github.silviacristinaa.auth.dtos.auth.LoginRequest;
import com.github.silviacristinaa.auth.dtos.auth.LoginResponse;
import com.github.silviacristinaa.auth.dtos.auth.ValidateTokenRequest;
import com.github.silviacristinaa.auth.dtos.auth.ValidateTokenResponse;
import com.github.silviacristinaa.auth.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.login(), request.password()));

        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());
        return new LoginResponse(token, "Bearer", jwtService.getExpiration());
    }

    @PostMapping("/validate")
    public ValidateTokenResponse validate(@RequestBody @Valid ValidateTokenRequest request) {
        String token = request.token();

        if (jwtService.isTokenExpired(token)) {
            return new ValidateTokenResponse(false, true, null, List.of());
        }

        if (!jwtService.isTokenValid(token)) {
            return new ValidateTokenResponse(false, false, null, List.of());
        }

        return new ValidateTokenResponse(
                true,
                false,
                jwtService.extractUsername(token),
                jwtService.extractRoles(token));
    }
}
