package kaua.felix.taskflow.domain.ports.out;

import kaua.felix.taskflow.domain.entity.Project;
import kaua.felix.taskflow.domain.shared.PageRequestDto;
import kaua.felix.taskflow.domain.shared.PageResponseDto;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepositoryPort {

    Project save (Project project);

    Optional<Project> findById(UUID id);

    PageResponseDto<Project> findByMemberId(UUID userId, PageRequestDto pageRequest);

}
