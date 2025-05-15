package com.example.backendpharmacie.auth;

public record AuthenticationRequest(
        String email,
        String motDePasse
) {}