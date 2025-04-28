package co.edu.udes.backend.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceRecordDTO {
    private Long studentId;
    private String studentName;
    private Long groupId;
    private String groupName;
    private String subjectName;
    private Map<LocalDate, Boolean> attendanceRecord;
    private int totalClasses;
    private int presentCount;
    private double attendancePercentage;
}