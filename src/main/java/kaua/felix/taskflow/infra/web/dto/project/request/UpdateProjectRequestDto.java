package kaua.felix.taskflow.infra.web.dto.project.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequestDto(
        @NotBlank(message = "The name field cannot be blank")
        String name,
        String description
) {
}
