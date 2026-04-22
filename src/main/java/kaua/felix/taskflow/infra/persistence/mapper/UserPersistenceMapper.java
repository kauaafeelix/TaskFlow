package kaua.felix.taskflow.infra.persistence.mapper;

import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.infra.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toEntity (UserJpaEntity userJpa){
        return new User(
                userJpa.getId(),
                userJpa.getName(),
                userJpa.getEmail(),
                userJpa.getPasswordHash(),
                userJpa.getAvatarUrl(),
                userJpa.getCreatedAt(),
                userJpa.getUpdatedAt()
        );
    }

    public UserJpaEntity toJpa (User user){
        return new UserJpaEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getAvatarUrl(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
