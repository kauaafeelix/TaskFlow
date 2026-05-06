package kaua.felix.taskflow.infra.web.mapper;

import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.infra.web.dto.user.response.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserWebMapper {

    public UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getCreatedAt()
        );
    }
}
