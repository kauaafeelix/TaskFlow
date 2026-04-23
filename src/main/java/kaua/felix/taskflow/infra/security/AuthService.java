package kaua.felix.taskflow.infra.security;

import kaua.felix.taskflow.domain.entity.User;
import kaua.felix.taskflow.domain.ports.in.AuthUseCase;
import kaua.felix.taskflow.domain.ports.out.TokenRepositoryPort;
import kaua.felix.taskflow.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final AuthenticationManager authenticationManager;
    private final UserRepositoryPort userRepository;
    private final TokenRepositoryPort tokenRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return tokenRepositoryPort.generateToken(user);
    }

    @Override
    public User register(String name, String email, String password) {

        if (userRepository.existsByEmail(email)){
            throw new RuntimeException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.create(name, email, encodedPassword);

        return userRepository.save(user);
    }
}
