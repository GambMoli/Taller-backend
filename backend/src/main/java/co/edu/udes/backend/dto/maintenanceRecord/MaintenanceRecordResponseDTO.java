package co.edu.udes.backend.dto.maintenanceRecord;


import co.edu.udes.backend.dto.material.MaterialDTO;
import lombok.Data;

import java.time.LocalDate;
@Data
public class MaintenanceRecordResponseDTO {
    private long id;
    private String code;
    private MaterialDTO material;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String responsible;
}
