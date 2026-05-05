package kaua.felix.taskflow.infra.persistence.adapter;

import jakarta.transaction.Transactional;
import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.exception.DomainException;
import kaua.felix.taskflow.domain.ports.out.TaskRepositoryPort;
import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.TaskJpaEntity;
import kaua.felix.taskflow.infra.persistence.mapper.CommentPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.mapper.TaskPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.mapper.UserPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.repository.ProjectJpaRepository;
import kaua.felix.taskflow.infra.persistence.repository.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository taskRepository;
    private final TaskPersistenceMapper taskMapper;
    private final ProjectJpaRepository projectRepository;
    private final CommentPersistenceMapper commentMapper;
    private final UserPersistenceMapper userMapper;

    @Override
    @Transactional
    public Task save(Task task) {
        ProjectJpaEntity projectJpa = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new DomainException("Project not found"));

        TaskJpaEntity jpaEntity = taskRepository.findById(task.getId())
                .map(existing -> {
                    existing.setTitle(task.getTitle());
                    existing.setDescription(task.getDescription());
                    existing.setStatus(task.getStatus());
                    existing.setPriority(task.getPriority());
                    existing.setDeadline(task.getDeadline());
                    existing.setUpdatedAt(task.getUpdatedAt());
                    existing.setAssignee(task.getAssignee() == null ? null : userMapper.toJpa(task.getAssignee()));
                    existing.getComments().clear();
                    existing.getComments().addAll(
                            task.getComments().stream()
                                    .map(comment -> commentMapper.toJpa(comment, existing))
                                    .collect(Collectors.toList())
                    );
                    return existing;
                })
                .orElseGet(() -> taskMapper.toJpa(task, projectJpa));

        TaskJpaEntity saved = taskRepository.save(jpaEntity);
        return taskMapper.toEntity(saved);
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return taskRepository.findById(id)
                .map(taskMapper::toEntity);
    }


    @Override
    public List<Task> findByProjectId(UUID projectId) {
        return taskRepository.findByProject_Id(projectId)
                .stream()
                .map(taskMapper::toEntity)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        taskRepository.deleteById(id);
    }
}
