package kaua.felix.taskflow.infra.web.dto.comment;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponseDto(
        UUID id,
        String content,
        UUID authorId,
        String authorName,
        LocalDateTime createdAt
) {
}
