package kaua.felix.taskflow.domain.service;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;
import kaua.felix.taskflow.domain.exception.DomainException;
import kaua.felix.taskflow.domain.exception.UnauthorizedOperationException;
import kaua.felix.taskflow.domain.ports.in.ProjectUseCase;
import kaua.felix.taskflow.domain.ports.out.ProjectRepositoryPort;
import kaua.felix.taskflow.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProjectService implements ProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final UserRepositoryPort userRepository;

    @Override
    public Project create(String name, String description, UUID ownerId) {

        User owner = userRepository.findById(ownerId)
                .orElseThrow(()-> new DomainException("User not found"));

        Project project = Project.create(name, description, owner);

        return projectRepository.save(project);
    }

    @Override
    public Project update(UUID projectId, String name, String description, UUID requesterId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new DomainException("Project not found"));

        if (!project.canEdit(requesterId)){
            throw new UnauthorizedOperationException("Requester does not have permission to edit this project");
        }

        project.update(name, description, requesterId);


        return projectRepository.save(project);
    }

    @Override
    public Project addMember(UUID projectId, UUID userId, ProjectRole role, UUID requesterId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new DomainException("Project not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new DomainException("User not found"));

        project.addMember(user, role, requesterId);

        return projectRepository.save(project);
    }

    @Override
    public Project removeMember(UUID projectId, UUID userId, UUID requesterId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new DomainException("Project not found"));

        project.removeMember(userId, requesterId);

        return projectRepository.save(project);

    }

    @Override
    public Project archive(UUID projectId, UUID requesterId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new DomainException("Project not found"));

        project.archive(requesterId);


        return projectRepository.save(project);
    }

    @Override
    public Project findById(UUID projectId, UUID requesterId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(()-> new DomainException("Project not found"));

        if (!project.isMember(requesterId)){
            throw new UnauthorizedOperationException("User is not a member of this project.");
        }

        return project;

    }

    @Override
    public List<Project> findByMemberId(UUID requesterId) {

        return projectRepository.findByMemberId(requesterId);
    }
}
