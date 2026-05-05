package kaua.felix.taskflow.infra.web.mapper;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.infra.web.dto.project.response.ProjectMemberResponseDto;
import kaua.felix.taskflow.infra.web.dto.project.response.ProjectResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProjectWebMapper {

    public ProjectResponseDto toDto(Project project) {
        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getMembers().stream()
                        .map(member -> new ProjectMemberResponseDto(
                                member.getUser().getId(),
                                member.getUser().getName(),
                                member.getRole()
                        ))
                        .toList(),
                project.getCreatedAt()
        );
    }
}
