package co.edu.udes.backend.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkAttendanceDTO {
    private List<Long> studentIds;
    private Long groupId;
    private LocalDate classDate;
    private Boolean present;
    private Long periodId;
    private Long registeredById;
}