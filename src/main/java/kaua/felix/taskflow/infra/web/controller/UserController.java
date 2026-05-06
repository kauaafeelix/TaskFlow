package kaua.felix.taskflow.infra.web.controller;

import jakarta.validation.Valid;
import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.ports.in.UserUseCase;
import kaua.felix.taskflow.infra.web.dto.task.request.UpdateTaskRequestDto;
import kaua.felix.taskflow.infra.web.dto.user.request.ChangePasswordRequestDto;
import kaua.felix.taskflow.infra.web.dto.user.request.UpdateProfileRequestDto;
import kaua.felix.taskflow.infra.web.dto.user.response.UserResponseDto;
import kaua.felix.taskflow.infra.web.mapper.UserWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserWebMapper userWebMapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(
            @AuthenticationPrincipal UserDetails userDetails
        ){

        User user = userUseCase.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(userWebMapper.toDto(user));

    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
        ){

        User user = userUseCase.findByEmail(userDetails.getUsername());
        User updated = userUseCase.updateProfile(user.getId(), request.name(), request.avatarUrl());
        return ResponseEntity.ok(userWebMapper.toDto(updated));

    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword (
            @Valid @RequestBody ChangePasswordRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
        ){

        User user = userUseCase.findByEmail(userDetails.getUsername());
        userUseCase.changePassword(user.getId(), request.oldPassword(), request.newPassword());
        return ResponseEntity.noContent().build();

    }
}
