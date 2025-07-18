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

    public EventResponseDto(Long id, String title, String description, LocalDate startDate,
                            LocalDate endDate, int difficulty, String classification,
                            BigDecimal cost, String coordinatorName, List<String> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.difficulty = difficulty;
        this.classification = classification;
        this.cost = cost;
        this.coordinatorName = coordinatorName;
        this.images = images;
    }

    public EventResponseDto() {

    }
}
