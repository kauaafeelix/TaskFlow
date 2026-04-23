package kaua.felix.taskflow.infra.persistence.adapter;

import kaua.felix.taskflow.domain.entity.Comment;
import kaua.felix.taskflow.domain.ports.out.CommentRepositoryPort;
import kaua.felix.taskflow.infra.persistence.entity.CommentJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.TaskJpaEntity;
import kaua.felix.taskflow.infra.persistence.mapper.CommentPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.repository.CommentJpaRepository;
import kaua.felix.taskflow.infra.persistence.repository.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommentRepositoryAdapter implements CommentRepositoryPort {

    private final CommentJpaRepository commentRepository;
    private final CommentPersistenceMapper commentMapper;
    private final TaskJpaRepository taskRepository;

    @Override
    public Comment save(Comment comment) {
        TaskJpaEntity taskJpa = taskRepository.findById(comment.getTaskId())
                .orElseThrow(()-> new RuntimeException("Task not found"));

        CommentJpaEntity commentJpa = commentMapper.toJpa(comment, taskJpa);

        commentRepository.save(commentJpa);

        return commentMapper.toEntity(commentJpa);
    }

    @Override
    public List<Comment> findByTaskId(UUID taskId) {
        return commentRepository.findByTask_Id(taskId)
                .stream()
                .map(commentMapper::toEntity)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        commentRepository.deleteById(id);
    }
}
