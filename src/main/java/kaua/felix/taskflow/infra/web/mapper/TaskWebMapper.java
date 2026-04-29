package kaua.felix.taskflow.infra.web.mapper;

import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.infra.web.dto.task.response.TaskResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TaskWebMapper {

    public TaskResponseDto toDto (Task task){
        return new TaskResponseDto(
                task.getId(),
                task.getProjectId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDeadline(),
                task.getAssignee() == null ? null :  task.getAssignee().getId(),
                task.getCreatedAt()
        );
    }
}
