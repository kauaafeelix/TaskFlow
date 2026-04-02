package kaua.felix.taskflow.domain.ports.in;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;

import java.util.List;
import java.util.UUID;

public interface ProjectUseCase {

    Project create (String name, String description, UUID ownerId);

    Project update (UUID projectId, String name, UUID ownerId);

    Project addMember(UUID projectId, UUID userId, ProjectRole role, UUID ownerId);

    Project removeMember (UUID projectId, UUID userId, UUID ownerId);

    Project archive (UUID projectId, UUID requesterId);

    Project findById (UUID projectId, UUID requesterId);

    List<Project> findByMemberId (UUID userId, UUID requesterId);
}
