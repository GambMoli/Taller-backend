package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.semester.SemesterCreateDTO;
import co.edu.udes.backend.dto.semester.SemesterResponseDTO;
import co.edu.udes.backend.dto.semester.SemesterUpdateDTO;
import co.edu.udes.backend.models.Semester;
import co.edu.udes.backend.service.SemesterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semesters")
public class SemesterController {
    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    @PostMapping
    public ResponseEntity<Semester> createSemester(@RequestBody SemesterCreateDTO dto) {
        Semester semester = new Semester();
        semester.setNumber(dto.getNumber());

        Semester createdSemester = semesterService.create(semester, dto.getCareerId());
        return new ResponseEntity<>(createdSemester, HttpStatus.CREATED);
    }

    @GetMapping("/career/{careerId}")
    public ResponseEntity<List<SemesterResponseDTO>> getSemestersByCareer(@PathVariable Long careerId) {
        List<SemesterResponseDTO> semesters = semesterService.getAllByCareerId(careerId);
        return ResponseEntity.ok(semesters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SemesterResponseDTO> getSemesterById(@PathVariable Long id) {
        SemesterResponseDTO semester = semesterService.getById(id);
        return ResponseEntity.ok(semester);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SemesterResponseDTO> updateSemester(
            @PathVariable Long id,
            @RequestBody SemesterUpdateDTO dto) {

        Semester semesterDetails = new Semester();
        semesterDetails.setNumber(dto.getNumber());

        SemesterResponseDTO updatedSemester = semesterService.update(id, semesterDetails);
        return ResponseEntity.ok(updatedSemester);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemester(@PathVariable Long id) {
        semesterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}