package co.edu.udes.backend.dto.maintenanceRecord;

import co.edu.udes.backend.models.Material;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MaintenanceRecordDTO {
    private long id;
    private String code;
    private long materialid;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String responsible;
}
