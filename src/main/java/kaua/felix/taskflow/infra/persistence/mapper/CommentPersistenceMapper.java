package kaua.felix.taskflow.infra.persistence.mapper;

import kaua.felix.taskflow.domain.entity.Comment;
import kaua.felix.taskflow.infra.persistence.entity.CommentJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.TaskJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentPersistenceMapper {

    private final UserPersistenceMapper userMapper;
    public Comment toEntity (CommentJpaEntity jpaEntity){

        return new Comment(
                jpaEntity.getId(),
                jpaEntity.getTask().getId(),
                userMapper.toEntity(jpaEntity.getAuthor()),
                jpaEntity.getContent(),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt()
        );
    }

    public CommentJpaEntity toJpa(Comment domain, TaskJpaEntity taskJpa) {
        return new CommentJpaEntity(
                domain.getId(),
                taskJpa,
                userMapper.toJpa(domain.getAuthor()),
                domain.getContent(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

}
