package com.example.testpostalapplication.error_handling;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Hidden
public class ErrorResponse {
    private final String error;
}

