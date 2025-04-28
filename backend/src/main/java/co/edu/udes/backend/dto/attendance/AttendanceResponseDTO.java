package co.edu.udes.backend.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDTO {
    private Long id;
    private String studentName;
    private String groupName;
    private LocalDate classDate;
    private Boolean present;
    private String periodName;
}