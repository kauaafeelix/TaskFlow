package kaua.felix.taskflow.infra.persistence.mapper;

import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.TaskJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TaskPersistenceMapper {

    private final UserPersistenceMapper userMapper;
    private final CommentPersistenceMapper commentMapper;


    public Task toEntity (TaskJpaEntity jpaEntity){
        return new Task(
          jpaEntity.getId(),
          jpaEntity.getProject().getId(),
          jpaEntity.getTitle(),
          jpaEntity.getDescription(),
          jpaEntity.getStatus(),
          jpaEntity.getPriority(),
          jpaEntity.getDeadline(),
          userMapper.toEntity(jpaEntity.getAssignee()),
                jpaEntity.getComments() == null
                        ? List.of()
                        : jpaEntity.getComments()
                          .stream()
                          .map(commentMapper::toEntity)
                          .collect(Collectors.toList()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt()
        );
    }

    public TaskJpaEntity toJpa(Task task, ProjectJpaEntity projectJpa) {

        TaskJpaEntity taskJpa = new TaskJpaEntity(
                task.getId(),
                projectJpa,
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDeadline(),
                task.getAssignee() == null ? null : userMapper.toJpa(task.getAssignee()),
                List.of(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );

        taskJpa.setComments(
                task.getComments() == null
                        ? List.of()
                        : task.getComments()
                          .stream()
                          .map(comment -> commentMapper.toJpa(comment, taskJpa))
                          .collect(Collectors.toList())
        );

        return taskJpa;
    }

}
