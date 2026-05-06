package kaua.felix.taskflow.domain.shared;

import java.util.List;

public record PageResponseDto<T>(
        List<T> content,
        int currentPage,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean last
) {
}
