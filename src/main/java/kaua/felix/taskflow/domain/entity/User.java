package kaua.felix.taskflow.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class User {
    private final UUID id;
    private String name;
    private String email;
    private String passwordHash;
    private String avatarUrl;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(UUID id, String name, String email, String passwordHash,
                String avatarUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(String name, String email, String passwordHash) {
        if (name == null || name.isBlank())
            throw new RuntimeException("Nome do usuário não pode ser vazio");
        if (email == null || !email.contains("@"))
            throw new RuntimeException("E-mail inválido");
        if (passwordHash == null || passwordHash.isBlank())
            throw new RuntimeException("Senha não pode ser vazia");

        LocalDateTime now = LocalDateTime.now();
        return new User(UUID.randomUUID(), name, email, passwordHash, null, now, now);
    }

    public void updateProfile(String name, String avatarUrl) {
        if (name == null || name.isBlank())
            throw new RuntimeException("Nome do usuário não pode ser vazio");
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String newPasswordHash) {
        if (newPasswordHash == null || newPasswordHash.isBlank())
            throw new RuntimeException("Senha não pode ser vazia");
        this.passwordHash = newPasswordHash;
        this.updatedAt = LocalDateTime.now();
    }



    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
