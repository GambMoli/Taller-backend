package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.grade.AssignGradeDTO;
import co.edu.udes.backend.dto.period.PeriodCreateDTO;
import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import co.edu.udes.backend.dto.period.PeriodUpdateDTO;
import co.edu.udes.backend.dto.subject.SubjectFinalGradeDTO;
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
    public ResponseEntity<PeriodResponseDTO> create(@RequestBody PeriodCreateDTO dto) {
        return new ResponseEntity<>(periodService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(periodService.getById(id));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<PeriodResponseDTO>> getAllByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(periodService.getAllByStudentId(studentId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PeriodResponseDTO> update(@PathVariable Long id,@RequestBody PeriodUpdateDTO dto) {
        return ResponseEntity.ok(periodService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        periodService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculate-final/{periodId}")
    public ResponseEntity<Void> calculateFinalGrades(@PathVariable Long periodId) {
        periodService.calculateFinalGrades(periodId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/generate-periods/{studentId}")
    public ResponseEntity<Void> generatePeriodsForStudent(@PathVariable Long studentId) {
        periodService.generatePeriodsForStudent(studentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign-grade")
    public ResponseEntity<PeriodResponseDTO> assignGrade( @RequestBody AssignGradeDTO dto) {
        return ResponseEntity.ok(periodService.assignGrade(dto));
    }

    @GetMapping("/calculate-subject-final/{studentId}/{subjectId}")
    public ResponseEntity<List<SubjectFinalGradeDTO>> calculateFinalSubjectGrades(
            @PathVariable Long studentId,
            @PathVariable Long subjectId) {
        return ResponseEntity.ok(periodService.calculateFinalSubjectGrades(studentId, subjectId));
    }

    @GetMapping("/calculate-all-subject-finals/{studentId}")
    public ResponseEntity<List<SubjectFinalGradeDTO>> calculateAllFinalSubjectGrades(@PathVariable Long studentId) {
        return ResponseEntity.ok(periodService.calculateAllFinalSubjectGrades(studentId));
    }
}