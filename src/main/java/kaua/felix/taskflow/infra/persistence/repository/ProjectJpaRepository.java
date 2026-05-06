package kaua.felix.taskflow.infra.persistence.repository;

import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, UUID> {
    Page<ProjectJpaEntity> findByMembers_User_Id(UUID userId, Pageable pageable);

}
