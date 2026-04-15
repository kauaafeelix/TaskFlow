package kaua.felix.taskflow.infra.persistence.mapper;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.infra.persistence.entity.ProjectMemberJpaEntity;
import kaua.felix.taskflow.infra.persistence.entity.ProjectJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectPersistenceMapper {

    private final ProjectMemberPersistenceMapper projectMemberPersistenceMapper;

    public Project toEntity (ProjectJpaEntity jpaEntity){

        return new Project(
                jpaEntity.getId(),
                jpaEntity.getName(),
                jpaEntity.getDescription(),
                jpaEntity.getStatus(),
                jpaEntity.getMembers() == null ? Collections.emptyList() : jpaEntity.getMembers()
                        .stream()
                        .map(projectMemberPersistenceMapper::toEntity)
                        .collect(Collectors.toList()),
                jpaEntity.getCreatedAt(),
                jpaEntity.getUpdatedAt()
        );
    }

    public ProjectJpaEntity toJpa (Project project){

        ProjectJpaEntity projectJpa = new ProjectJpaEntity(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                Collections.emptyList(),
                project.getCreatedAt(),
                project.getUpdateAt()
        );

        List<ProjectMemberJpaEntity> membersJpa = project.getMembers() == null ? Collections.emptyList() : project.getMembers()
                .stream()
                .map(member -> projectMemberPersistenceMapper.toJpa(member, projectJpa))
                .collect(Collectors.toList());

        projectJpa.setMembers(membersJpa);

        return projectJpa;
    }
}
