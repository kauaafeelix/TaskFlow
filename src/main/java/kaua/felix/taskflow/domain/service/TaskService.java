package kaua.felix.taskflow.domain.service;

import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;
import kaua.felix.taskflow.domain.ports.in.TaskUseCase;
import kaua.felix.taskflow.domain.ports.out.ProjectRepositoryPort;
import kaua.felix.taskflow.domain.ports.out.TaskRepositoryPort;
import kaua.felix.taskflow.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TaskService implements TaskUseCase {

    private final TaskRepositoryPort taskRepositoryPort;
    private final ProjectRepositoryPort projectRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;


    @Override
    public Task create(UUID projectId, String title, String description, TypePriority priority, LocalDate deadline, UUID assigneeId, UUID requesterId) {
        return null;
    }

    @Override
    public Task update(UUID taskId, String title, String description, TypePriority priority, LocalDate deadline, UUID requesterId) {
        return null;
    }

    @Override
    public Task changeStatus(UUID taskId, TaskStatus newStatus, UUID requesterId) {
        return null;
    }

    @Override
    public Task assign(UUID taksId, UUID assigneeId, UUID requesterId) {
        return null;
    }

    @Override
    public Task findById(UUID taskId, UUID requesterId) {
        return null;
    }

    @Override
    public List<Task> findByProjectId(UUID projectId, UUID requesterId) {
        return List.of();
    }

    @Override
    public void delete(UUID taskId, UUID requesterId) {

    }
}
