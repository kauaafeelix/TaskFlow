package kaua.felix.taskflow.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class CommentJpaEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskJpaEntity task;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity author;

    @Column(nullable = false)
    private String content;

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

    public CommentJpaEntity(UUID id, TaskJpaEntity task, UserJpaEntity author, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.task = task;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
