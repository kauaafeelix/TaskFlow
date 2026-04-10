package kaua.felix.taskflow.infra.persistence.mapper;

import kaua.felix.taskflow.domain.entity.ProjectMember;
import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.ProjectMemberJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMemberPersistenceMapper {

    private final UserPersistenceMapper userMapper;

    public ProjectMember toEntity(ProjectMemberJpaEntity jpa) {
        return new ProjectMember(
                jpa.getId(),
                jpa.getProject().getId(),
                userMapper.toEntity(jpa.getUser()),
                jpa.getRole(),
                jpa.getJoinedAt()
        );
    }

    public ProjectMemberJpaEntity toJpa(ProjectMember domain, ProjectJpaEntity projectJpa) {
        return new ProjectMemberJpaEntity(
                domain.getId(),
                projectJpa,
                userMapper.toJpa(domain.getUser()),
                domain.getRole(),
                domain.getJoinedAt()
        );
    }
}
