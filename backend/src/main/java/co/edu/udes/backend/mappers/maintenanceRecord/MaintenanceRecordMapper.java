package co.edu.udes.backend.mappers.maintenanceRecord;

import co.edu.udes.backend.dto.maintenanceRecord.MaintenanceRecordDTO;
import co.edu.udes.backend.dto.maintenanceRecord.MaintenanceRecordResponseDTO;
import co.edu.udes.backend.models.MaintenanceRecord;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MaintenanceRecordMapper {
    MaintenanceRecord toEntity (MaintenanceRecordDTO maintenanceRecordDTO);
    MaintenanceRecordResponseDTO toResponseDTO (MaintenanceRecord maintenanceRecord);
}
