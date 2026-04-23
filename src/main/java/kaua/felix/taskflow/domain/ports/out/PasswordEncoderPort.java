package kaua.felix.taskflow.domain.ports.out;

public interface PasswordEncoderPort {
    String encode (String rawPassord);
    boolean matches (String rawPassword, String encodedPassword);
}
