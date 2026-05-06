package kaua.felix.taskflow.domain.ports.in;

import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;
import kaua.felix.taskflow.domain.shared.PageRequestDto;
import kaua.felix.taskflow.domain.shared.PageResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskUseCase {

    Task create (UUID projectId, String title, String description, TypePriority priority,
                 LocalDate deadline, UUID assigneeId, UUID requesterId);

    Task update (UUID taskId, String title, String description,
                 TypePriority priority, LocalDate deadline, UUID requesterId);

    Task changeStatus (UUID taskId, TaskStatus newStatus, UUID requesterId);

    Task assign (UUID taskId, UUID assigneeId, UUID requesterId);

    Task findById (UUID taskId, UUID requesterId);

    PageResponseDto<Task> findByProjectId(UUID projectId, UUID requesterId, PageRequestDto pageRequest);

    void delete (UUID taskId, UUID requesterId);

    Task addComment(UUID taskId, String content, UUID requesterId);

    void removeComment(UUID taskId, UUID commentId, UUID requesterId);
}
