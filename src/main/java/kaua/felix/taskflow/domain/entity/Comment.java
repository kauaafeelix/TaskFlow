package kaua.felix.taskflow.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class Comment {

    private final UUID id;
    private final UUID taskId;
    private final User author;
    private String content;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Comment(UUID id, UUID taskId, User author, String content,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.taskId = taskId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Comment create(UUID taskId, User author, String content) {
        if (content == null || content.isBlank())
            throw new RuntimeException("Conteúdo do comentário não pode ser vazio");
        LocalDateTime now = LocalDateTime.now();
        return new Comment(UUID.randomUUID(), taskId, author, content, now, now);
    }

    public void edit(String newContent, UUID requesterId) {
        if (!this.author.getId().equals(requesterId))
            throw new RuntimeException("Apenas o autor pode editar o comentário");
        if (newContent == null || newContent.isBlank())
            throw new RuntimeException("Conteúdo do comentário não pode ser vazio");
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }


    public UUID getId() {
        return id;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
