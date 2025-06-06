package co.edu.udes.backend.dto.period;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PeriodCreateDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double weight;
    private Long studentId;
}