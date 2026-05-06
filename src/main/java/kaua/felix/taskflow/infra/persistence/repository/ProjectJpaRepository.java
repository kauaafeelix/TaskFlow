package kaua.felix.taskflow.infra.persistence.repository;

import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectJpaRepository extends JpaRepository<ProjectJpaEntity, UUID> {
    List<ProjectJpaEntity> findByMembers_User_Id(UUID userId);

}
