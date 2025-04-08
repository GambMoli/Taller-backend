package co.edu.udes.backend.dto.subject;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectStatusDTO {
    private Long id;
    private String name;
    private Integer semester;
    private String status; // "COMPLETED", "IN_PROGRESS", "PENDING"
}

