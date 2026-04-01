package kaua.felix.taskflow.domain.entity;

import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProjectMember {
    private final UUID id;
    private final UUID projectId;
    private final User user;
    private ProjectRole role;
    private final LocalDateTime joinedAt;

    public ProjectMember(UUID id, UUID projectId, User user,
                         ProjectRole role, LocalDateTime joinedAt) {
        this.id = id;
        this.projectId = projectId;
        this.user = user;
        this.role = role;
        this.joinedAt = joinedAt;
    }


    public static ProjectMember create(UUID projectId, User user, ProjectRole role) {
        return new ProjectMember(UUID.randomUUID(), projectId, user, role, LocalDateTime.now());
    }

    public void changeRole(ProjectRole newRole) {
        this.role = newRole;
    }
    public boolean isOwner() {
        return this.role == ProjectRole.OWNER;
    }
    public boolean canEdit() {
        return this.role == ProjectRole.OWNER || this.role == ProjectRole.MEMBER;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public User getUser() {
        return user;
    }

    public ProjectRole getRole() {
        return role;
    }

    public void setRole(ProjectRole role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
