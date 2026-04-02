package kaua.felix.taskflow.domain.service;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;
import kaua.felix.taskflow.domain.ports.in.ProjectUseCase;

import java.util.List;
import java.util.UUID;

public class ProjectService implements ProjectUseCase {
    @Override
    public Project create(String name, String description, UUID ownerId) {
        return null;
    }

    @Override
    public Project update(UUID projectId, String name, UUID ownerId) {
        return null;
    }

    @Override
    public Project addMember(UUID projectId, UUID userId, ProjectRole role, UUID ownerId) {
        return null;
    }

    @Override
    public Project removeMember(UUID projectId, UUID userId, UUID ownerId) {
        return null;
    }

    @Override
    public Project archive(UUID projectId, UUID requesterId) {
        return null;
    }

    @Override
    public Project findById(UUID projectId, UUID requesterId) {
        return null;
    }

    @Override
    public List<Project> findByMemberId(UUID userId, UUID requesterId) {
        return List.of();
    }
}
