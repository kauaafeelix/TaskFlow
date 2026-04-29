package kaua.felix.taskflow.infra.web.dto.project.request;

import jakarta.validation.constraints.NotNull;
import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;

import java.util.UUID;

public record AddMemberRequestDto(
        @NotNull(message = "The user field cannot be null")
        UUID userId,
        @NotNull(message = "The role field cannot be null")
        ProjectRole role
) {
}
