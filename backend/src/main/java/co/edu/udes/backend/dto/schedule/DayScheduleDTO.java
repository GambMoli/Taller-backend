package co.edu.udes.backend.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayScheduleDTO {
    private String dayName;
    private List<ClassScheduleDTO> classes = new ArrayList<>(); // Clases del día
}