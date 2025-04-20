package co.edu.udes.backend.dto.period;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PeriodResponseDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double weight;
    private Long semesterId;
    private String semesterName;
}
