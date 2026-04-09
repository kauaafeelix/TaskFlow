package kaua.felix.taskflow.domain.ports.out;

import kaua.felix.taskflow.domain.entity.Project;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepositoryPort {

    Project save (Project project);

    Optional<Project> findById(UUID id);

    List<Project> findByMemberId(UUID userId);


}
