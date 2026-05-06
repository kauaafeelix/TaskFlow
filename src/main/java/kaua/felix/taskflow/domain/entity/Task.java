package kaua.felix.taskflow.domain.entity;

import kaua.felix.taskflow.domain.entity.enuns.TaskStatus;
import kaua.felix.taskflow.domain.entity.enuns.TypePriority;
import kaua.felix.taskflow.domain.exception.DomainException;
import kaua.felix.taskflow.domain.exception.UnauthorizedOperationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {

    private final UUID id;
    private final UUID projectId;
    private String title;
    private String description;
    private TaskStatus status;
    private TypePriority priority;
    private LocalDate deadline;
    private User assignee;
    private final List<Comment> comments;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(UUID id, UUID projectId, String title, String description,
                TaskStatus status, TypePriority priority, LocalDate deadline,
                User assignee, List<Comment> comments,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.deadline = deadline;
        this.assignee = assignee;
        this.comments = new ArrayList<>(comments);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Task(UUID id, UUID projectId, String title, String description, List<Comment> comments, LocalDateTime createdAt) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public static Task create(UUID projectId, String title, String description,
                              TypePriority priority, LocalDate deadline, User assignee) {
            if (title == null || title.isBlank()) {
                throw new DomainException("The task title cannot be empty.");
            }
            if (deadline != null && deadline.isBefore(LocalDate.now())) {
                throw new DomainException("The due date cannot be in the past.");
            }
            LocalDateTime now = LocalDateTime.now();
            return new Task(UUID.randomUUID(), projectId, title, description,
                    TaskStatus.TODO, priority, deadline, assignee,
                    new ArrayList<>(), now, now);
        }

    public void update(String title, String description, TypePriority priority, LocalDate deadline) {
        if (title == null || title.isBlank())
            throw new DomainException("The task title cannot be empty.");
        if (this.status == TaskStatus.DONE || this.status == TaskStatus.CANCELLED)
            throw new DomainException("It is not possible to edit a completed task.\n");
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.updatedAt = LocalDateTime.now();
    }

        public void changeStatus(TaskStatus newStatus) {
            validateStatusTransition(this.status, newStatus);

            this.status = newStatus;
            this.updatedAt = LocalDateTime.now();

        }

        public void assign (User user){

            if (this.status == TaskStatus.DONE || this.status == TaskStatus.CANCELLED) {
                throw new DomainException("Cannot assign a completed task");
            }
            this.assignee = user;
            this.updatedAt = LocalDateTime.now();
        }

        public void unassign (){
            this.assignee = null;
            this.updatedAt = LocalDateTime.now();
        }

        public Comment addComment (User author, String content){
            Comment comment = Comment.create(this.id, author, content);
            this.comments.add(comment);
            this.updatedAt = LocalDateTime.now();
            return comment;
        }

        public void removeComment(UUID commentId, UUID requesterId) {
            Comment comment = this.comments.stream()
                    .filter(c -> c.getId().equals(commentId))
                    .findFirst()
                    .orElseThrow(() -> new DomainException("Comment not found"));

            if (!comment.getAuthor().getId().equals(requesterId)) {
                throw new UnauthorizedOperationException("Only the author can remove a comment");
            }

            this.comments.remove(comment);
            this.updatedAt = LocalDateTime.now();
    }

        public boolean isOverdue(){
            return deadline != null
                    && LocalDateTime.now().isAfter(deadline.atStartOfDay())
                    && status != TaskStatus.DONE
                    && status != TaskStatus.CANCELLED;
        }

    private void validateStatusTransition(TaskStatus current, TaskStatus next) {
        boolean valid = switch (current) {
            case TODO        -> next == TaskStatus.IN_PROGRESS || next == TaskStatus.CANCELLED;
            case IN_PROGRESS -> next == TaskStatus.IN_REVIEW || next == TaskStatus.TODO || next == TaskStatus.CANCELLED;
            case IN_REVIEW   -> next == TaskStatus.DONE || next == TaskStatus.IN_PROGRESS;
            case DONE, CANCELLED -> false;
        };
        if (!valid)
            throw new DomainException("Invalid status transition: " + current + " -> " + next);
    }

    public UUID getId() {
        return id;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TypePriority getPriority() {
        return priority;
    }

    public void setPriority(TypePriority priority) {
        this.priority = priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public List<Comment> getComments() {
        return comments;
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
