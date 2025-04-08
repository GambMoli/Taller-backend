package co.edu.udes.backend.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerEnrollmentDTO {
    private Long studentId;
    private Long careerId;
}