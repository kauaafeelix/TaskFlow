package kaua.felix.taskflow.infra.web.controller;

import jakarta.validation.Valid;
import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.ports.in.ProjectUseCase;
import kaua.felix.taskflow.domain.ports.in.TaskUseCase;
import kaua.felix.taskflow.domain.ports.in.UserUseCase;
import kaua.felix.taskflow.infra.web.dto.project.request.AddMemberRequestDto;
import kaua.felix.taskflow.infra.web.dto.project.request.CreateProjectRequestDto;
import kaua.felix.taskflow.infra.web.dto.project.request.RemoveMemberRequestDto;
import kaua.felix.taskflow.infra.web.dto.project.request.UpdateProjectRequestDto;
import kaua.felix.taskflow.infra.web.dto.project.response.ProjectDetailResponseDto;
import kaua.felix.taskflow.infra.web.dto.project.response.ProjectResponseDto;
import kaua.felix.taskflow.infra.web.dto.task.request.CreateTaskRequestDto;
import kaua.felix.taskflow.infra.web.dto.task.response.TaskResponseDto;
import kaua.felix.taskflow.infra.web.mapper.ProjectWebMapper;
import kaua.felix.taskflow.infra.web.mapper.TaskWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectUseCase projectUseCase;
    private final ProjectWebMapper projectWebMapper;
    private final TaskUseCase taskUseCase;
    private final TaskWebMapper taskWebMapper;
    private final UserUseCase userUseCase;

    @PostMapping
    public ResponseEntity<ProjectResponseDto> create (
            @Valid @RequestBody CreateProjectRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID ownerId = userUseCase.findByEmail(userDetails.getUsername()).getId();

        Project project = projectUseCase.create(request.name(), request.description(), ownerId);

        return ResponseEntity.status(HttpStatus.CREATED).body(projectWebMapper.toDto(project));

    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskResponseDto> createTask (

            @PathVariable UUID projectId,
            @Valid @RequestBody CreateTaskRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails

    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Task task = taskUseCase.create(projectId, request.title(), request.description(), request.priority(), request.deadline(), request.assigneeId(), requesterId);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskWebMapper.toDto(task));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDto> update (
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProjectRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Project project = projectUseCase.update(id, request.name(), request.description(), requesterId);
        return ResponseEntity.ok(projectWebMapper.toDto(project));

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailResponseDto> findById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Project project = projectUseCase.findById(id, requesterId);
        List<Task> tasks = taskUseCase.findByProjectId(id, requesterId);
        return ResponseEntity.ok(projectWebMapper.toDetailDto(project, tasks));
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<TaskResponseDto>> findByProjectId (
            @PathVariable UUID projectId,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        List<Task> tasks = taskUseCase.findByProjectId(projectId, requesterId);

        return ResponseEntity.ok(tasks.stream()
                .map(taskWebMapper::toDto)
                .toList());
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> findByMemberId (
            @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        List<Project> projects = projectUseCase.findByMemberId(requesterId);

        return ResponseEntity.ok(projects.stream()
                .map(projectWebMapper::toDto)
                .toList());
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ProjectResponseDto> addMember(
            @PathVariable UUID id,
            @Valid @RequestBody AddMemberRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        UUID userId = userUseCase.findByEmail(request.email()).getId();
        Project project = projectUseCase.addMember(id, userId, request.role(), requesterId);
        return ResponseEntity.ok(projectWebMapper.toDto(project));
    }

    @DeleteMapping("/{id}/members")
    public ResponseEntity<Void> removeMember(
            @PathVariable UUID id,
            @Valid @RequestBody RemoveMemberRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        UUID userId = userUseCase.findByEmail(request.email()).getId();
        projectUseCase.removeMember(id, userId, requesterId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ProjectResponseDto> archive (
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        Project project = projectUseCase.archive(id, requesterId);
        return ResponseEntity.ok(projectWebMapper.toDto(project));

    }
}
