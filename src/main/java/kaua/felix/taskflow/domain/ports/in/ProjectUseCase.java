package kaua.felix.taskflow.domain.ports.in;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;

import java.util.List;
import java.util.UUID;

public interface ProjectUseCase {

    Project create (String name, String description, UUID ownerId);

    Project update (UUID projectId, String name, String description, UUID requesterId);

    Project addMember(UUID projectId, UUID userId, ProjectRole role, UUID requester);

    Project removeMember (UUID projectId, UUID userId, UUID requester);

    Project archive (UUID projectId, UUID requesterId);

    Project findById (UUID projectId, UUID requesterId);

    List<Project> findByMemberId (UUID requesterId);
}
