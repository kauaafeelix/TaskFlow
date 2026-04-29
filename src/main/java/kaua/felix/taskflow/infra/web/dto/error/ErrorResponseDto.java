package kaua.felix.taskflow.infra.web.dto.error;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        int status,
        LocalDateTime timestamp
) {
}
