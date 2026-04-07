package kaua.felix.taskflow.domain.service;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.entity.User;
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

        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.isMember(requesterId)) {
            throw new RuntimeException("Requester is not a member of the project");
        }

        if (!project.canEdit(requesterId)){
            throw new RuntimeException("Requester does not have permission to create tasks in this project");
        }

        User assignee = null;

        if (assigneeId != null){
            assignee = userRepositoryPort.findById(assigneeId)
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));

            if (!project.isMember(assigneeId)){
                throw new RuntimeException("Assignee is not a member of the project");
            }
        }

        Task task = Task.create(projectId, title, description, priority, deadline, assignee);

        return taskRepositoryPort.save(task);
    }

    @Override
    public Task update(UUID taskId, String title, String description, TaskStatus status,
                       TypePriority priority, LocalDate deadline, UUID requesterId) {

        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        if (!project.canEdit(requesterId)) {
            throw new RuntimeException("Você não tem permissão para editar esta tarefa");
        }

        task.update(title, description, status, priority, deadline);

        return taskRepositoryPort.save(task);
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
