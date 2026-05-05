package kaua.felix.taskflow.infra.web.dto.task.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AssignRequestDto(
        @NotBlank(message = "The email field cannot be blank")
        @Email(message = "The email address must be valid")
        String email
) {
}
