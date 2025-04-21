package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.maintenanceRecord.MaintenanceRecordDTO;
import co.edu.udes.backend.dto.maintenanceRecord.MaintenanceRecordResponseDTO;
import co.edu.udes.backend.service.MaintenanceRecordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/record_maintenance")
public class MaintenanceController {
    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceController(MaintenanceRecordService maintenanceRecordService){
        this.maintenanceRecordService=maintenanceRecordService;
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceRecordResponseDTO>> getAlls(){
        return ResponseEntity.ok(maintenanceRecordService.getAlls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponseDTO> getOne(@PathVariable long id){
        return ResponseEntity.ok(maintenanceRecordService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<MaintenanceRecordResponseDTO> createMaintenance(@RequestBody MaintenanceRecordDTO maintenanceRecordDTO){
        return ResponseEntity.ok(maintenanceRecordService.createMaintenance(maintenanceRecordDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponseDTO> modifyMaintenance(@PathVariable long id, @RequestBody MaintenanceRecordDTO maintenanceRecordDTO){
        return ResponseEntity.ok(maintenanceRecordService.modifyMaintenance(id,maintenanceRecordDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponseDTO> deleteMaintenance(@PathVariable long id){
        maintenanceRecordService.deleteMaintenance(id);
        return ResponseEntity.noContent().build();
    }
}