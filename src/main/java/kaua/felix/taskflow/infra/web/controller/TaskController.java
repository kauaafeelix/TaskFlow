package kaua.felix.taskflow.infra.web.controller;

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
public class TaskController {

    private final TaskUseCase taskUseCase;
    private final UserUseCase userUseCase;
    private final TaskWebMapper taskWebMapper;


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

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> findById (
        @PathVariable UUID id,
        @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Task task = taskUseCase.findById(id, requesterId);
        return ResponseEntity.ok(taskWebMapper.toDto(task));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        taskUseCase.delete(id, requesterId);
        return ResponseEntity.noContent().build();
    }

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
