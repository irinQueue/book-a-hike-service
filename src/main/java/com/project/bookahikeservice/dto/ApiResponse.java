package com.project.bookahikeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private List<T> data;
    private List<String> errors;
}
