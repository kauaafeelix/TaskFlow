package kaua.felix.taskflow.domain.shared;

import kaua.felix.taskflow.domain.exception.DomainException;

public record PageRequestDto(
        int page,
        int size
) {

    public PageRequestDto {
        if (page < 0) throw new DomainException("Page must be greater than or equal to 0");
        if (size < 1 || size > 100) throw new DomainException("Size must be between 1 and 100");
    }
}
