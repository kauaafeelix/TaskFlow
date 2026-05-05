package kaua.felix.taskflow.infra.persistence.adapter;

import jakarta.transaction.Transactional;
import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.ports.out.ProjectRepositoryPort;
import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import kaua.felix.taskflow.infra.persistence.mapper.ProjectPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor

public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final ProjectJpaRepository projectRepository;
    private final ProjectPersistenceMapper projectMapper;

    @Override
    @Transactional
    public Project save(Project project) {

        ProjectJpaEntity jpaEntity = projectMapper.toJpa(project);

        ProjectJpaEntity saved = projectRepository.save(jpaEntity);

        return projectMapper.toEntity(saved);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return projectRepository.findById(id)
                .map(projectMapper::toEntity);
    }

    @Override
    public List<Project> findByMemberId(UUID userId) {
        return projectRepository.findByMembers_User_Id(userId)
                .stream()
                .map(projectMapper::toEntity)
                .toList();
    }
}
