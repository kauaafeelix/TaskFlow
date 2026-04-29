package kaua.felix.taskflow.infra.web.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @NotBlank(message = "The name cannot be left blank.")
        String name,
        @NotBlank(message = "The email cannot be left blank.")
        @Email(message = "The email address must be valid.")
        String email,
        @NotBlank(message = "The password cannot be left blank")
        String password
) {
}
