package com.project.bookahikeservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    @Column
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private int difficulty; // 1 - 9

    private String classification; // major or minor

    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "coordinator_id")
    private User coordinator;

    @ElementCollection
    private List<String> images;
}
