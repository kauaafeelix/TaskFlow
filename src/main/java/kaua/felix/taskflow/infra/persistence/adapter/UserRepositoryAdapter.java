package kaua.felix.taskflow.infra.persistence.adapter;

import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.ports.out.UserRepositoryPort;
import kaua.felix.taskflow.infra.persistence.entity.UserJpaEntity;
import kaua.felix.taskflow.infra.persistence.mapper.UserPersistenceMapper;
import kaua.felix.taskflow.infra.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    public User save(User user) {
        UserJpaEntity userJpa = userMapper.toJpa(user);

        userRepository.save(userJpa);

        return userMapper.toEntity(userJpa);

    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
