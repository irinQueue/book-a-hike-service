package com.project.bookahikeservice.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int difficulty;
    private String classification;
    private BigDecimal cost;
    private String coordinatorName;
    private List<String> images;
}
