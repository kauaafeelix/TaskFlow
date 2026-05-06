package kaua.felix.taskflow.infra.web.dto.project.response;

import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;

import java.util.UUID;

public record ProjectMemberResponseDto(
        String email,
        String name,
        ProjectRole role
) {
}