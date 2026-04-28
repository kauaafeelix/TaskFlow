package kaua.felix.taskflow.infra.web.dto.task.request;

import jakarta.validation.constraints.NotNull;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;

public record ChangeStatusRequestDto(
        @NotNull(message = "The status field cannot be null")
        TaskStatus status
) {
}
