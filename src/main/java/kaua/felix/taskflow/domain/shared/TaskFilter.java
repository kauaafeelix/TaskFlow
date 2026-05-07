package kaua.felix.taskflow.domain.shared;

import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;

import java.time.LocalDate;

public record TaskFilter(
        TaskStatus status,
        TypePriority priority,
        LocalDate deadline
) {
}
