package kaua.felix.taskflow.domain.service;

import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.ports.in.UserUseCase;
import kaua.felix.taskflow.domain.ports.out.PasswordEncoderPort;
import kaua.felix.taskflow.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;


    @Override
    public User findById(UUID id) {
        return userRepositoryPort.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));
    }

    @Override
    public User updateProfile(UUID userId, String name, String avatarUrl) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));


        user.updateProfile(name, avatarUrl);

        return userRepositoryPort.save(user);
    }

    @Override
    public User changePassword(UUID userId, String oldPassword, String newPassword) {
        User user = userRepositoryPort.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        if (!passwordEncoderPort.matches(oldPassword, user.getPasswordHash())){
            throw new RuntimeException("Old password is incorrect");
        }

        String encodedNewPassword = passwordEncoderPort.encode(newPassword);
        user.changePassword(encodedNewPassword);

        return userRepositoryPort.save(user);
    }
}
