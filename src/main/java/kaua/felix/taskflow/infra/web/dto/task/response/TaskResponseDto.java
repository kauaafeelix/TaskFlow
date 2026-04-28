package kaua.felix.taskflow.infra.web.dto.task.response;

import kaua.felix.taskflow.domain.entity.enuns.ProjectStatus;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponseDto(
        UUID id,
        UUID projectId,
        String title,
        String description,
        TaskStatus status,
        TypePriority priority,
        LocalDate deadline,
        UUID assignee,
        LocalDateTime createdAt
) {
}
