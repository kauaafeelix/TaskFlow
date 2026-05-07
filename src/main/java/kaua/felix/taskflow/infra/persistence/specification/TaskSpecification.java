package kaua.felix.taskflow.infra.persistence.specification;

import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;
import kaua.felix.taskflow.infra.persistence.entity.TaskJpaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

public class TaskSpecification {

    public static Specification<TaskJpaEntity> byProjectId(UUID projectId) {
        return (root, query, cb)
                -> cb.equal(root.get("project").get("id"), projectId);
    }

    public static Specification<TaskJpaEntity> byStatus(TaskStatus status) {
        return (root, query, cb)
                -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<TaskJpaEntity> byPriority(TypePriority priority) {
        return (root, query, cb)
                -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    public static Specification<TaskJpaEntity> byDeadlineBefore(LocalDate deadline) {
        return (root, query, cb)
                -> deadline == null ? null : cb.lessThanOrEqualTo(root.get("deadline"), deadline);
    }
}
