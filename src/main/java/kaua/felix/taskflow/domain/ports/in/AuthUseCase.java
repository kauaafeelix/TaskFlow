package kaua.felix.taskflow.domain.ports.in;

import kaua.felix.taskflow.domain.entity.User;

public interface AuthUseCase {

    String login (String email, String password);

    String register (String name, String email, String password);
}
