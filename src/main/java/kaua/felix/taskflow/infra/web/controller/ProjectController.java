package kaua.felix.taskflow.infra.web.controller;

import jakarta.validation.Valid;
import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.Task;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;
import kaua.felix.taskflow.domain.ports.in.ProjectUseCase;
import kaua.felix.taskflow.domain.ports.in.TaskUseCase;
import kaua.felix.taskflow.domain.ports.in.UserUseCase;
import kaua.felix.taskflow.domain.shared.PageRequestDto;
import kaua.felix.taskflow.domain.shared.PageResponseDto;
import kaua.felix.taskflow.domain.shared.TaskFilter;
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

import java.time.LocalDate;
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
        PageResponseDto<Task> tasks = taskUseCase.findByProjectId(id, requesterId, new TaskFilter(null, null, null), new PageRequestDto(0, 100));
        return ResponseEntity.ok(projectWebMapper.toDetailDto(project, tasks.content()));
    }   

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<PageResponseDto<TaskResponseDto>> findByProjectId(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TypePriority priority,
            @RequestParam(required = false) LocalDate deadline
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        PageRequestDto pageRequest = new PageRequestDto(page, size);
        TaskFilter filter = new TaskFilter(status, priority, deadline);
        PageResponseDto<Task> tasks = taskUseCase.findByProjectId(projectId, requesterId, filter, pageRequest);

        return ResponseEntity.ok(new PageResponseDto<>(
                tasks.content().stream()
                        .map(taskWebMapper::toDto)
                        .toList(),
                tasks.currentPage(),
                tasks.pageSize(),
                tasks.totalElements(),
                tasks.totalPages(),
                tasks.last()
        ));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<ProjectResponseDto>> findByMemberId(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UUID requesterId = userUseCase.findByEmail(userDetails.getUsername()).getId();
        PageRequestDto pageRequest = new PageRequestDto(page, size);
        PageResponseDto<Project> projects = projectUseCase.findByMemberId(requesterId, pageRequest);

        return ResponseEntity.ok(new PageResponseDto<>(
                projects.content().stream()
                        .map(projectWebMapper::toDto)
                        .toList(),
                projects.currentPage(),
                projects.pageSize(),
                projects.totalElements(),
                projects.totalPages(),
                projects.last()
        ));
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
