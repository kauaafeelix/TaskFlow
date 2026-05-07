package kaua.felix.taskflow.infra.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.ports.in.UserUseCase;
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
@Tag(name = "Users", description = "Endpoints for user profile management")
public class UserController {

    private final UserUseCase userUseCase;
    private final UserWebMapper userWebMapper;

    @Operation(summary = "Get current user", description = "Returns the authenticated user's profile data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(
            @AuthenticationPrincipal UserDetails userDetails
        ){

        User user = userUseCase.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(userWebMapper.toDto(user));

    }

    @Operation(summary = "Update profile", description = "Updates the authenticated user's name and avatar URL")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "User not found"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateProfile(
            @Valid @RequestBody UpdateProfileRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
        ){

        User user = userUseCase.findByEmail(userDetails.getUsername());
        User updated = userUseCase.updateProfile(user.getId(), request.name(), request.avatarUrl());
        return ResponseEntity.ok(userWebMapper.toDto(updated));

    }

    @Operation(summary = "Change password", description = "Changes the authenticated user's password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "403", description = "Old password is incorrect"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
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
