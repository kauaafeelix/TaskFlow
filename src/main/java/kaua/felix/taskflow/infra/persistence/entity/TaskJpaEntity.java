package kaua.felix.taskflow.infra.persistence.entity;

import jakarta.persistence.*;
import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskJpaEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectJpaEntity project;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePriority priority;

    @Column(nullable = false)
    private LocalDate deadline;

    @ManyToOne
    @JoinColumn(name = "assignee_id", nullable = true)
    private UserJpaEntity assignee;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentJpaEntity> comments;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public TaskJpaEntity(UUID id, ProjectJpaEntity project, String title, String description, TaskStatus status, TypePriority priority,
                         LocalDate deadline, UserJpaEntity assignee, List<CommentJpaEntity> comments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.project = project;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.deadline = deadline;
        this.assignee = assignee;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
