package kaua.felix.taskflow.infra.web.dto.task.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTaskRequestDto(
        @NotBlank(message = "The title field cannot be blank")
        String title,
        String description,
        @NotNull(message = "The priority field cannot be null")
        TypePriority priority,
        @NotNull(message = "The deadline field cannot be null")
        LocalDate deadline,
        UUID assigneeId
) {
}
