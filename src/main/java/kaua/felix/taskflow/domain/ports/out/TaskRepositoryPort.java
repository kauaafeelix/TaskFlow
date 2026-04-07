package kaua.felix.taskflow.domain.ports.out;

import kaua.felix.taskflow.domain.entity.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {

    Task save (Task task);

    Optional<Task> findById(UUID id);

    List<Task> findByProjectId(UUID projectId);

    void delete(UUID id);

}
