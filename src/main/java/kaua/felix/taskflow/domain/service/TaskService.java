package kaua.felix.taskflow.domain.service;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;
import kaua.felix.taskflow.domain.exception.DomainException;
import kaua.felix.taskflow.domain.exception.UnauthorizedOperationException;
import kaua.felix.taskflow.domain.ports.in.TaskUseCase;
import kaua.felix.taskflow.domain.ports.out.ProjectRepositoryPort;
import kaua.felix.taskflow.domain.ports.out.TaskRepositoryPort;
import kaua.felix.taskflow.domain.ports.out.UserRepositoryPort;
import kaua.felix.taskflow.domain.shared.PageRequestDto;
import kaua.felix.taskflow.domain.shared.PageResponseDto;
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
                .orElseThrow(() -> new DomainException("Project not found"));

        if (!project.isMember(requesterId)) {
            throw new UnauthorizedOperationException("Requester is not a member of the project");
        }

        if (!project.canEdit(requesterId)){
            throw new UnauthorizedOperationException("Requester does not have permission to create tasks in this project");
        }

        User assignee = null;

        if (assigneeId != null){
            assignee = userRepositoryPort.findById(assigneeId)
                    .orElseThrow(() -> new DomainException("Assignee not found"));

            if (!project.isMember(assigneeId)){
                throw new UnauthorizedOperationException("Assignee is not a member of the project");
            }
        }

        Task task = Task.create(projectId, title, description, priority, deadline, assignee);

        return taskRepositoryPort.save(task);
    }

    @Override
    public Task update(UUID taskId, String title, String description,
                       TypePriority priority, LocalDate deadline, UUID requesterId) {

        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new DomainException("Task not found"));

        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(() -> new DomainException("Project not found"));

        if (!project.canEdit(requesterId)) {
            throw new UnauthorizedOperationException("Requester does not have permission to create tasks in this project");
        }

        task.update(title, description, priority, deadline);

        return taskRepositoryPort.save(task);
    }

    @Override
    public Task changeStatus(UUID taskId, TaskStatus newStatus, UUID requesterId) {

        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new DomainException("Task not found"));
        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(()-> new DomainException("Project not found"));

        if (!project.canEdit(requesterId)){
            throw new UnauthorizedOperationException("Requester does not have permission to edit tasks in this project");
        }

        task.changeStatus(newStatus);

        return taskRepositoryPort.save(task);
    }

    @Override
    public Task assign(UUID taskId, UUID assigneeId, UUID requesterId) {
        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new DomainException("Task not found"));
        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(() -> new DomainException("Project not found"));

        if (!project.canEdit(requesterId)){
            throw new UnauthorizedOperationException("Requester does not have permission to edit tasks in this project");
        }

        User assignee = userRepositoryPort.findById(assigneeId)
                .orElseThrow(()-> new DomainException("User not found"));

        if (!project.isMember(assigneeId)){
            throw new UnauthorizedOperationException("User is not a member of the project.");
        }

        task.assign(assignee);

        return taskRepositoryPort.save(task);
    }

    @Override
    public Task findById(UUID taskId, UUID requesterId) {

        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new DomainException("Task not found"));
        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(() -> new DomainException("Project not found"));

        if (!project.isMember(requesterId)){
            throw new UnauthorizedOperationException("User is not a member of the project.    ");
        }

        return task;
    }

    @Override
    public PageResponseDto<Task> findByProjectId(UUID projectId, UUID requesterId, PageRequestDto pageRequest) {
        Project project = projectRepositoryPort.findById(projectId)
                .orElseThrow(() -> new DomainException("Project not found"));

        if (!project.isMember(requesterId)) {
            throw new UnauthorizedOperationException("User is not a member of the project.");
        }

        return taskRepositoryPort.findByProjectId(projectId, pageRequest);
    }

    @Override
    public void delete(UUID taskId, UUID requesterId) {
        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new DomainException("Task not found"));

        Project project = projectRepositoryPort.findById(task.getProjectId())
                        .orElseThrow(()-> new DomainException("Project not found"));

        if (!project.canEdit(requesterId)){
            throw new UnauthorizedOperationException("You don't have permission to delete this task.");
        }

        taskRepositoryPort.delete(taskId);
    }

    @Override
    public Task addComment(UUID taskId, String content, UUID requesterId) {
        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new DomainException("Task not found"));

        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(() -> new DomainException("Project not found"));

        if (!project.isMember(requesterId)) {
            throw new UnauthorizedOperationException("User is not a member of the project");
        }

        User user = userRepositoryPort.findById(requesterId)
                .orElseThrow(() -> new DomainException("User not found"));

        task.addComment(user, content);

        return taskRepositoryPort.save(task);

    }

    @Override
    public void removeComment(UUID taskId, UUID commentId, UUID requesterId) {

        Task task = taskRepositoryPort.findById(taskId)
                .orElseThrow(() -> new DomainException("Task not found"));

        Project project = projectRepositoryPort.findById(task.getProjectId())
                .orElseThrow(() -> new DomainException("Project not found"));

        if (!project.isMember(requesterId)) {
            throw new UnauthorizedOperationException("User is not a member of the project");
        }

        task.removeComment(commentId, requesterId);

        taskRepositoryPort.save(task);
    }
}
