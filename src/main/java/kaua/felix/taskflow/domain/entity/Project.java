package kaua.felix.taskflow.domain.entity;

import kaua.felix.taskflow.domain.entity.enuns.ProjectRole;
import kaua.felix.taskflow.domain.entity.enuns.ProjectStatus;
import org.hibernate.sql.results.DomainResultCreationException;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {

    private final UUID id;
    private String name;
    private String description;
    private ProjectStatus status;
    private List<ProjectMember> members;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Project(UUID id, String name, String description, ProjectStatus status,
                   List<ProjectMember> members, LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.members = new ArrayList<>(members);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Project create (String name, String description, User owner) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("O nome do projeto não pode ser vazio");
        }
            LocalDateTime now = LocalDateTime.now();
            Project project = new Project(UUID.randomUUID(), name, description,
                    ProjectStatus.ACTIVE, new ArrayList<>(), now, now);

            project.members.add(ProjectMember.create(project.id, owner, ProjectRole.OWNER));

            return project;

    }

    public void update (String name, String description, UUID requesterId){
        ensureCanEdit(requesterId);

        if (name == null || name.isBlank()){
            throw new RuntimeException("O nome do projeto não pode ser vazio");
        }
        this.name = name;
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }

    public void addMember(User user, ProjectRole role, UUID requesterId) {
        ensureIsOwner(requesterId);
        boolean alreadyMember = members.stream()
                .anyMatch(m -> m.getUser().getId().equals(user.getId()));
        if (alreadyMember)
            throw new RuntimeException("Usuário já é membro deste projeto");
        members.add(ProjectMember.create(this.id, user, role));
        this.updatedAt = LocalDateTime.now();
    }

    public void removeMember(UUID userId, UUID requesterId) {
        ensureIsOwner(requesterId);
        if (userId.equals(requesterId))
            throw new RuntimeException("O owner não pode remover a si mesmo do projeto");
        boolean removed = members.removeIf(m -> m.getUser().getId().equals(userId));
        if (!removed)
            throw new RuntimeException("Usuário não encontrado no projeto");
        this.updatedAt = LocalDateTime.now();
    }

    public void changeMemberRole(UUID userId, ProjectRole newRole, UUID requesterId) {
        ensureIsOwner(requesterId);
        findMember(userId).changeRole(newRole);
        this.updatedAt = LocalDateTime.now();
    }

    public void archive(UUID requesterId) {
        ensureIsOwner(requesterId);
        if (this.status == ProjectStatus.ARCHIVED)
            throw new RuntimeException("Projeto já está arquivado");
        this.status = ProjectStatus.ARCHIVED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isMember(UUID userId) {
        return members.stream().anyMatch(m -> m.getUser().getId().equals(userId));
    }

    public boolean canEdit(UUID userId) {
        return members.stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .anyMatch(ProjectMember::canEdit);
    }

    private void ensureCanEdit(UUID requesterId) {
        if (!canEdit(requesterId))
            throw new RuntimeException("Sem permissão para editar este projeto");
    }

    private void ensureIsOwner(UUID requesterId) {
        boolean isOwner = members.stream()
                .filter(m -> m.getUser().getId().equals(requesterId))
                .anyMatch(ProjectMember::isOwner);
        if (!isOwner)
            throw new RuntimeException("Apenas o owner pode executar esta operação");
    }

    private ProjectMember findMember(UUID userId) {
        return members.stream()
                .filter(m -> m.getUser().getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Membro não encontrado no projeto"));
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public List<ProjectMember> getMembers() {
        return members;
    }

    public void setMembers(List<ProjectMember> members) {
        this.members = members;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updatedAt;
    }

    public void setUpdateAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
