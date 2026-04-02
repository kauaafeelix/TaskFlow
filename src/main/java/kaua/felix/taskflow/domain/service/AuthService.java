package kaua.felix.taskflow.domain.service;

import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.ports.in.AuthCase;

public class AuthService implements AuthCase {
    @Override
    public String login(String email, String password) {
        return "";
    }

    @Override
    public User register(String name, String email, String password) {
        return null;
    }
}
