package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.grade.AssignGradeDTO;
import co.edu.udes.backend.dto.period.PeriodCreateDTO;
import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import co.edu.udes.backend.dto.period.PeriodUpdateDTO;
import co.edu.udes.backend.service.PeriodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periods")
public class PeriodController {
    private final PeriodService periodService;

    public PeriodController(PeriodService periodService) {
        this.periodService = periodService;
    }

    @PostMapping
    public ResponseEntity<PeriodResponseDTO> createPeriod(@RequestBody PeriodCreateDTO dto) {
        return new ResponseEntity<>(periodService.create(dto), HttpStatus.CREATED);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> getPeriod(@PathVariable Long id) {
        return ResponseEntity.ok(periodService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> updatePeriod(
            @PathVariable Long id,
            @RequestBody PeriodUpdateDTO dto) {
        return ResponseEntity.ok(periodService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePeriod(@PathVariable Long id) {
        periodService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/student/{studentId}/generate")
    public ResponseEntity<Void> generatePeriodsForStudent(@PathVariable Long studentId) {
        periodService.generatePeriodsForStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/grades")
    public ResponseEntity<PeriodResponseDTO> assignGrade(@RequestBody AssignGradeDTO dto) {
        PeriodResponseDTO response = periodService.assignGrade(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{periodId}/calculate-final")
    public ResponseEntity<Void> calculateFinalGrades(@PathVariable Long periodId) {
        periodService.calculateFinalGrades(periodId);
        return ResponseEntity.ok().build();
    }

}