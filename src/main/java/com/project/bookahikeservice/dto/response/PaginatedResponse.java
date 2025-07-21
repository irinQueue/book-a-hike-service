package com.project.bookahikeservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> data;
    private PageableDetails pageable;
    private List<String> errors;
    private String message;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageableDetails {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean isLast;
    }
}
