package kaua.felix.taskflow.infra.web.mapper;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.infra.web.dto.project.response.ProjectDetailResponseDto;
import kaua.felix.taskflow.infra.web.dto.project.response.ProjectMemberResponseDto;
import kaua.felix.taskflow.infra.web.dto.project.response.ProjectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectWebMapper {

    private final TaskWebMapper taskWebMapper;

    public ProjectResponseDto toDto(Project project) {
        return new ProjectResponseDto(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getMembers().stream()
                        .map(member -> new ProjectMemberResponseDto(
                                member.getUser().getEmail(),
                                member.getUser().getName(),
                                member.getRole()
                        ))
                        .toList(),
                project.getCreatedAt()
        );
    }

    public ProjectDetailResponseDto toDetailDto(Project project, List<Task> tasks) {
        return new ProjectDetailResponseDto(
                project.getId(),
                project.getName(),
                project.getStatus(),
                project.getMembers().stream()
                        .map(member -> new ProjectMemberResponseDto(
                                member.getUser().getEmail(),
                                member.getUser().getName(),
                                member.getRole()
                        ))
                        .toList(),
                tasks.stream()
                        .map(taskWebMapper::toDto)
                        .toList(),
                project.getCreatedAt()
        );
    }
}
