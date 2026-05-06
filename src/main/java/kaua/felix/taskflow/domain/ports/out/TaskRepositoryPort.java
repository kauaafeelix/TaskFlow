package kaua.felix.taskflow.domain.ports.out;

import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.shared.PageRequestDto;
import kaua.felix.taskflow.domain.shared.PageResponseDto;

import java.util.Optional;
import java.util.UUID;

public interface TaskRepositoryPort {

    Task save (Task task);

    Optional<Task> findById(UUID id);

    PageResponseDto<Task> findByProjectId(UUID projectId, PageRequestDto pageRequest);

    void delete(UUID id);

}
