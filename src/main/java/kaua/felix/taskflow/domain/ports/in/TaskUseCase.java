package kaua.felix.taskflow.domain.ports.in;

import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskUseCase {

    Task create (UUID projectId, String title, String description, TypePriority priority,
                 LocalDate deadline, UUID assigneeId, UUID requesterId);

    Task update (UUID taskId, String title, String description,
                 TypePriority priority, LocalDate deadline, UUID requesterId);

    Task changeStatus (UUID taskId, TaskStatus newStatus, UUID requesterId);

    Task assign (UUID taksId, UUID assigneeId, UUID requesterId);

    Task findById (UUID taskId, UUID requesterId);

    List<Task> findByProjectId (UUID projectId, UUID requesterId);

    void delete (UUID taskId, UUID requesterId);


}
