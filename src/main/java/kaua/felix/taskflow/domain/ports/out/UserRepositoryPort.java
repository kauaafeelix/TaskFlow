package kaua.felix.taskflow.domain.ports.out;

import kaua.felix.taskflow.domain.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save (User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

}
