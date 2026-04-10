package kaua.felix.taskflow.infra.persistence.repository;

import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.ProjectMemberJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepository extends JpaRepository<ProjectMemberJpaEntity, UUID> {
    Optional<ProjectMemberJpaEntity> findByProjectIdAndUserId(UUID projectId, UUID userId);

}
