package kaua.felix.taskflow.infra.web.dto.user.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String name,
        String email,
        String avatarUrl,
        LocalDateTime createdAt
) {
}
