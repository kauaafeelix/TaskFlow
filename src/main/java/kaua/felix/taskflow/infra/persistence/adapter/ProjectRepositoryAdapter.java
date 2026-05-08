package kaua.felix.taskflow.infra.persistence.adapter;

import jakarta.transaction.Transactional;
import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.ports.out.ProjectRepositoryPort;
import kaua.felix.taskflow.domain.shared.PageRequestDto;
import kaua.felix.taskflow.domain.shared.PageResponseDto;
import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import kaua.felix.taskflow.infra.persistence.mapper.ProjectMemberPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.mapper.ProjectPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor

public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final ProjectJpaRepository projectRepository;
    private final ProjectPersistenceMapper projectMapper;
    private final ProjectMemberPersistenceMapper projectMemberMapper;

    @Override
    @Transactional
    public Project save(Project project) {
        ProjectJpaEntity jpaEntity = projectRepository.findById(project.getId())
                .map(existing -> {
                    existing.setName(project.getName());
                    existing.setDescription(project.getDescription());
                    existing.setStatus(project.getStatus());
                    existing.setUpdatedAt(project.getUpdatedAt());
                    existing.getMembers().clear();
                    existing.getMembers().addAll(
                            project.getMembers().stream()
                                    .map(member -> projectMemberMapper.toJpa(member, existing))
                                    .toList()
                    );
                    return existing;
                })
                .orElseGet(() -> projectMapper.toJpa(project));

        ProjectJpaEntity saved = projectRepository.save(jpaEntity);
        return projectMapper.toEntity(saved);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return projectRepository.findById(id)
                .map(projectMapper::toEntity);
    }

    @Override
    public PageResponseDto<Project> findByMemberId(UUID userId, PageRequestDto pageRequest) {
        PageRequest springPageRequest =
                PageRequest.of(pageRequest.page(), pageRequest.size());

        Page<ProjectJpaEntity> page = projectRepository.findByMembers_User_Id(userId, springPageRequest);

        return new PageResponseDto<>(
                page.getContent().stream()
                        .map(projectMapper::toEntity)
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
