package kaua.felix.taskflow.infra.persistence.adapter;

import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.ports.out.TaskRepositoryPort;
import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.TaskJpaEntity;
import kaua.felix.taskflow.infra.persistence.mapper.TaskPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.repository.ProjectJpaRepository;
import kaua.felix.taskflow.infra.persistence.repository.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository taskRepository;
    private final TaskPersistenceMapper taskMapper;
    private final ProjectJpaRepository projectRepository;

    @Override
    public Task save(Task task) {
        ProjectJpaEntity projectJpa = projectRepository.findById(task.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not Found"));
        TaskJpaEntity taskJpa = taskMapper.toJpa(task, projectJpa);

        taskRepository.save(taskJpa);

        return taskMapper.toEntity(taskJpa);
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
