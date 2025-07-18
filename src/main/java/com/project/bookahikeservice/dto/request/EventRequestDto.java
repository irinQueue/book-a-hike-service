package com.project.bookahikeservice.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

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
    private String classification; // You can later switch to enum for stricter validation

    @DecimalMin("0.0")
    private BigDecimal cost;

    @NotNull(message = "Coordinator ID is required")
    private Long coordinatorId;

    // Optional list of image files
    private List<String> images;
}
