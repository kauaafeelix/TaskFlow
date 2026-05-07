package kaua.felix.taskflow.infra.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.ports.in.TaskUseCase;
import kaua.felix.taskflow.domain.ports.in.UserUseCase;
import kaua.felix.taskflow.infra.web.dto.task.request.*;
import kaua.felix.taskflow.infra.web.dto.task.response.TaskResponseDto;
import kaua.felix.taskflow.infra.web.mapper.TaskWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Endpoints for task management")
public class TaskController {

    private final TaskUseCase taskUseCase;
    private final UserUseCase userUseCase;
    private final TaskWebMapper taskWebMapper;


    @Operation(summary = "Update task", description = "Updates title, description, priority and deadline of a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to edit"),
            @ApiResponse(responseCode = "400", description = "Task not found"),
            @ApiResponse(responseCode = "422", description = "Validation error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update (
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
        ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Task task = taskUseCase.update(id, request.title(), request.description(), request.priority(), request.deadline(), requesterId);
        return ResponseEntity.ok(taskWebMapper.toDto(task));

    }

    @Operation(summary = "Change task status", description = "Changes the status of a task following allowed transitions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status transition"),
            @ApiResponse(responseCode = "403", description = "User is not a member of the project")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponseDto> changeStatus(
        @PathVariable UUID id,
        @Valid @RequestBody ChangeStatusRequestDto request,
        @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Task task = taskUseCase.changeStatus(id, request.status(), requesterId);
        return ResponseEntity.ok(taskWebMapper.toDto(task));

    }

    @Operation(summary = "Assign task", description = "Assigns a task to a user by email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task assigned successfully"),
            @ApiResponse(responseCode = "400", description = "Task not found or cannot be assigned"),
            @ApiResponse(responseCode = "403", description = "User is not a member of the project")
    })
    @PatchMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDto> assign(
            @PathVariable UUID id,
            @Valid @RequestBody AssignRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        UUID assigneeId = userUseCase.findByEmail(request.email()).getId();
        Task task = taskUseCase.assign(id, assigneeId, requesterId);
        return ResponseEntity.ok(taskWebMapper.toDto(task));
    }

    @Operation(summary = "Get task by id", description = "Returns task details with comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "400", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "User is not a member of the project")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findById (
        @PathVariable UUID id,
        @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Task task = taskUseCase.findById(id, requesterId);
        return ResponseEntity.ok(taskWebMapper.toDto(task));

    }

    @Operation(summary = "Delete task", description = "Deletes a task permanently")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "User does not have permission to delete")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        taskUseCase.delete(id, requesterId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add comment", description = "Adds a comment to a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment added successfully"),
            @ApiResponse(responseCode = "400", description = "Task not found"),
            @ApiResponse(responseCode = "403", description = "User is not a member of the project")
    })
    @PostMapping("/{id}/comments")
    public ResponseEntity<TaskResponseDto> addComment(
            @PathVariable UUID id,
            @Valid @RequestBody AddCommentRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Task task = taskUseCase.addComment(id, request.content(), requesterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskWebMapper.toDto(task));
    }

    @Operation(summary = "Remove comment", description = "Removes a comment from a task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment removed successfully"),
            @ApiResponse(responseCode = "400", description = "Comment not found"),
            @ApiResponse(responseCode = "403", description = "Only the author can remove the comment")
    })
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<Void> removeComment(
            @PathVariable UUID id,
            @PathVariable UUID commentId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        taskUseCase.removeComment(id, commentId, requesterId);
        return ResponseEntity.noContent().build();
    }
}
