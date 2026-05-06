package kaua.felix.taskflow.infra.web.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequestDto(

        @NotBlank(message = "The old password field cannot be blank")
        String oldPassword,
        @NotBlank(message = "The new password field cannot be blank")
        @Size(min = 6, message = "The new password must have at least 6 characters")
        String newPassword

) {
}

