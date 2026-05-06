package kaua.felix.taskflow.infra.persistence.repository;

import kaua.felix.taskflow.infra.persistence.entity.TaskJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskJpaRepository extends JpaRepository<TaskJpaEntity, UUID> {
    Page<TaskJpaEntity> findByProject_Id(UUID projectId, Pageable pageable);

}
