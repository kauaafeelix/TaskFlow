package kaua.felix.taskflow.domain.ports.out;

import kaua.felix.taskflow.domain.entity.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentRepositoryPort {

    Comment save(Comment comment);

    List<Comment> findByTaskId(UUID taskId);

    void delete(UUID id);
}
