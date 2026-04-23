package kaua.felix.taskflow.domain.ports.out;

import kaua.felix.taskflow.domain.entity.User;

public interface TokenRepositoryPort {
    String generateToken(User user);
}
