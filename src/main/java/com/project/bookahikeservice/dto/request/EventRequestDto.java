package com.project.bookahikeservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class EventRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Min(1)
    @Max(9)
    private int difficulty;

    @NotBlank(message = "Classification is required")
    private String classification;

    @DecimalMin("0.0")
    private BigDecimal cost;

    @NotNull(message = "Coordinator ID is required")
    private Long coordinatorId;

    private List<String> images;

//    @NotNull(message = "Created by user ID is required")
//    private Long createdBy;
//
//    @NotNull(message = "Updated by user ID is required")
//    private Long updatedBy;
}
