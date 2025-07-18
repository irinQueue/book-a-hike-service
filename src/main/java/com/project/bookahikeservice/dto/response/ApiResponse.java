package com.project.bookahikeservice.dto.response;

import com.project.bookahikeservice.entity.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private List<T> data;
    private List<String> errors;

}
