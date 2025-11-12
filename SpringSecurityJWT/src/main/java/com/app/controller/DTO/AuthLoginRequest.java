package com.app.controller.DTO;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(@NotBlank String username, @NotBlank String password) {
}
