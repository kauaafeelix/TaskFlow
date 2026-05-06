package kaua.felix.taskflow.infra.web.dto.task.request;

import jakarta.validation.constraints.NotBlank;

public record AddCommentRequestDto(
        @NotBlank(message = "The content field cannot be blank")
        String content
) {
}