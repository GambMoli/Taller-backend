package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.subject.SubjectCreateDTO;
import co.edu.udes.backend.dto.subject.SubjectResponseDTO;
import co.edu.udes.backend.dto.subject.SubjectUpdateDTO;
import co.edu.udes.backend.models.Subject;
import co.edu.udes.backend.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(@RequestBody SubjectCreateDTO dto) {
        Subject subject = new Subject();
        subject.setName(dto.getName());
        subject.setPrerequisites(dto.getPrerequisites());

        Subject createdSubject = subjectService.create(subject, dto.getSemesterId());
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
        // O usar el método optimizado:
        // return ResponseEntity.ok(subjectService.getAllSubjectsOptimized());
    }

    @GetMapping("/{id}")
    public SubjectResponseDTO findById(@PathVariable Long id) {
        return subjectService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponseDTO> updateSubject(
            @PathVariable Long id,
            @RequestBody SubjectUpdateDTO dto) {

        Subject subjectDetails = new Subject();
        subjectDetails.setName(dto.getName());
        subjectDetails.setPrerequisites(dto.getPrerequisites());

        SubjectResponseDTO updatedSubject = subjectService.update(id, subjectDetails, dto.getSemesterId());
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        subjectService.delete(id);
    }

    @PostMapping("/{subjectId}/semester/{semesterId}")
    public ResponseEntity<Void> assignSubjectToSemester(
            @PathVariable Long subjectId,
            @PathVariable Long semesterId) {

        subjectService.assignToSemester(subjectId, semesterId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/semester/{semesterId}")
    public ResponseEntity<List<SubjectResponseDTO>> getSubjectsBySemester(@PathVariable Long semesterId) {
        List<SubjectResponseDTO> subjects = subjectService.getSubjectsBySemester(semesterId);
        return ResponseEntity.ok(subjects);
    }
}
