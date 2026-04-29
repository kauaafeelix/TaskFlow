package kaua.felix.taskflow.infra.web.dto.project.response;

import kaua.felix.taskflow.domain.entity.enuns.ProjectStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponseDto(
        UUID id,
        String name,
        ProjectStatus status,
        LocalDateTime createdAt
) {
}
