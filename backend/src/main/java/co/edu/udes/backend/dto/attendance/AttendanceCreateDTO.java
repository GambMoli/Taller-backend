package co.edu.udes.backend.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceCreateDTO {
    private Long studentId;
    private Long groupId;
    private LocalDate classDate;
    private Boolean present;
    private Long periodId;
    private Long registeredById;
}