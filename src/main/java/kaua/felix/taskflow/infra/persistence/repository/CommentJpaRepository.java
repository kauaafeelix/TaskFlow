package kaua.felix.taskflow.infra.persistence.repository;

import kaua.felix.taskflow.infra.persistence.entity.CommentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentJpaRepository extends JpaRepository<CommentJpaEntity, UUID> {
    List<CommentJpaEntity> findByTask_Id(UUID taskId);

}
