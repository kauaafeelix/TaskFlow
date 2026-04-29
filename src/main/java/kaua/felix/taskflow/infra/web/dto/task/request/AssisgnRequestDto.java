package kaua.felix.taskflow.infra.web.dto.task.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssisgnRequestDto(
        @NotNull(message = "The assignee field cannot be null")
        UUID assigneeId
) {
}
