package com.example.studentmanagement.controller.dto;

import java.util.List;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public record PageResponse<T>(
    List<T> content,
    PageMetadata page
) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
            List.copyOf(page.getContent()),
            PageMetadata.from(page)
        );
    }

    public record PageMetadata(
        int number,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        int numberOfElements,
        List<SortDescriptor> sort
    ) {

        private static <T> PageMetadata from(Page<T> page) {
            List<SortDescriptor> sortDescriptors = StreamSupport.stream(page.getSort().spliterator(), false)
                .map(SortDescriptor::from)
                .toList();

            return new PageMetadata(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.getNumberOfElements(),
                sortDescriptors
            );
        }
    }

    public record SortDescriptor(
        String property,
        String direction,
        boolean ignoreCase,
        String nullHandling
    ) {
        private static SortDescriptor from(Sort.Order order) {
            return new SortDescriptor(
                order.getProperty(),
                order.getDirection().name(),
                order.isIgnoreCase(),
                order.getNullHandling().name()
            );
        }
    }
}
