package kaua.felix.taskflow.infra.web.dto.project.response;

import kaua.felix.taskflow.domain.entity.enuns.ProjectStatus;
import kaua.felix.taskflow.infra.web.dto.task.response.TaskResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ProjectDetailResponseDto(
        UUID id,
        String name,
        ProjectStatus status,
        List<ProjectMemberResponseDto> members,
        List<TaskResponseDto> tasks,
        LocalDateTime createdAt
) {
}