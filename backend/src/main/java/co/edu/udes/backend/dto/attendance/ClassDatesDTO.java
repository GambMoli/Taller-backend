package co.edu.udes.backend.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassDatesDTO {
    private Long groupId;
    private String groupName;
    private String subjectName;
    private Long periodId;
    private String periodName;
    private List<LocalDate> classDates;
}