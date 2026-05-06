package kaua.felix.taskflow.domain.ports.in;

import kaua.felix.taskflow.domain.entity.User;

import java.util.UUID;

public interface UserUseCase {
    User findById(UUID id);
    User updateProfile(UUID userId, String name, String avatarUrl);
    User changePassword(UUID userId, String oldPassword, String newPassword);
    User findByEmail(String email);
}
