package com.app.controller.DTO;

import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;
@Validated
public record AuthCreateRoleRequest(
        @Size(max = 3,message = "No puedes tener mas de 3 roles lol") List<String> roleListName
) {
}
