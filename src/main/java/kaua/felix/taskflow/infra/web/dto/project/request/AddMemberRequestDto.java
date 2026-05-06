package kaua.felix.taskflow.infra.web.dto.project.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;

import java.util.UUID;

public record AddMemberRequestDto(
        @NotBlank(message = "The email field cannot be blank")
        @Email(message = "The email address must be valid")
        String email,
        @NotNull(message = "The role field cannot be null")
        ProjectRole role
) {
}
