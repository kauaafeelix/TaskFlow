package kaua.felix.taskflow.infra.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequestDto(
        @NotBlank(message = "The name field cannot be blank")
        String name,
        String avatarUrl
) {
}
