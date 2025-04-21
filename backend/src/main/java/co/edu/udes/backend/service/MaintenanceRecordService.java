package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.maintenanceRecord.MaintenanceRecordDTO;
import co.edu.udes.backend.dto.maintenanceRecord.MaintenanceRecordResponseDTO;
import co.edu.udes.backend.mappers.maintenanceRecord.MaintenanceRecordMapper;
import co.edu.udes.backend.models.MaintenanceRecord;
import co.edu.udes.backend.models.Material;
import co.edu.udes.backend.repositories.MaintenanceRecordRepository;
import co.edu.udes.backend.repositories.MaterialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final MaintenanceRecordMapper maintenanceRecordMapper;
    private final MaterialRepository materialRepository;

    public MaintenanceRecordService(MaterialRepository materialRepository,MaintenanceRecordRepository maintenanceRecordRepository, MaintenanceRecordMapper maintenanceRecordMapper){
        this.maintenanceRecordRepository=maintenanceRecordRepository;
        this.maintenanceRecordMapper=maintenanceRecordMapper;
        this.materialRepository = materialRepository;
    }

    public List<MaintenanceRecordResponseDTO> getAlls(){
        return maintenanceRecordRepository.findAll().stream()
                .map(maintenanceRecordMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MaintenanceRecordResponseDTO getOne(long id){
        MaintenanceRecord maintenanceRecord= maintenanceRecordRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No existe ese mantenimiento"));

        return  maintenanceRecordMapper.toResponseDTO(maintenanceRecord);
    }

    public MaintenanceRecordResponseDTO createMaintenance(MaintenanceRecordDTO maintenanceRecordDTO) {
        // Buscar el material por ID
        Material material = materialRepository.findById(maintenanceRecordDTO.getMaterialid())
                .orElseThrow(() -> new RuntimeException("No existe el material con ID: " + maintenanceRecordDTO.getMaterialid()));


        MaintenanceRecord maintenanceRecord = new MaintenanceRecord();
        maintenanceRecord.setCode(maintenanceRecordDTO.getCode());
        maintenanceRecord.setMaterial(material);
        maintenanceRecord.setDescription(maintenanceRecordDTO.getDescription());
        maintenanceRecord.setStartDate(maintenanceRecordDTO.getStartDate());
        maintenanceRecord.setEndDate(maintenanceRecordDTO.getEndDate());
        maintenanceRecord.setStatus(maintenanceRecordDTO.getStatus());
        maintenanceRecord.setResponsible(maintenanceRecordDTO.getResponsible());

        // Guardar y devolver
        MaintenanceRecord savedMaintenance = maintenanceRecordRepository.save(maintenanceRecord);
        return maintenanceRecordMapper.toResponseDTO(savedMaintenance);
    }

    public MaintenanceRecordResponseDTO modifyMaintenance(long id, MaintenanceRecordDTO maintenanceRecordDTO) {

        MaintenanceRecord maintenanceRecord = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe ese mantenimiento"));


        Material material = materialRepository.findById(maintenanceRecordDTO.getMaterialid())
                .orElseThrow(() -> new RuntimeException("No existe el material con ID: " + maintenanceRecordDTO.getMaterialid()));


        maintenanceRecord.setCode(maintenanceRecordDTO.getCode());
        maintenanceRecord.setMaterial(material);
        maintenanceRecord.setDescription(maintenanceRecordDTO.getDescription());
        maintenanceRecord.setStartDate(maintenanceRecordDTO.getStartDate());
        maintenanceRecord.setEndDate(maintenanceRecordDTO.getEndDate());
        maintenanceRecord.setStatus(maintenanceRecordDTO.getStatus());
        maintenanceRecord.setResponsible(maintenanceRecordDTO.getResponsible());


        MaintenanceRecord savedMaintenance = maintenanceRecordRepository.save(maintenanceRecord);
        return maintenanceRecordMapper.toResponseDTO(savedMaintenance);
    }

    public void deleteMaintenance(long id){
        MaintenanceRecord maintenanceRecord= maintenanceRecordRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No existe ese mantenimiento"));
        maintenanceRecordRepository.deleteById(id);
    }
}
