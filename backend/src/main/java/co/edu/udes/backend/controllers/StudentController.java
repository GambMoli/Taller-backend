package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.academicRecord.AcademicRecordDTO;
import co.edu.udes.backend.dto.enrollment.CareerEnrollmentDTO;
import co.edu.udes.backend.dto.enrollment.EnrollmentDTO;
import co.edu.udes.backend.dto.schedule.ScheduleDTO;
import co.edu.udes.backend.dto.schedule.ScheduleStudentDTO;
import co.edu.udes.backend.dto.student.*;
import co.edu.udes.backend.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> create(@RequestBody StudentDTO studentDTO) {
        return new ResponseEntity<>(studentService.create(studentDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> findAll() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> update(@PathVariable Long id, @RequestBody StudentDTO studentDTO) {
        return ResponseEntity.ok(studentService.update(id, studentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/enroll-career")
    public ResponseEntity<StudentResponseDTO> enrollInCareer(@RequestBody CareerEnrollmentDTO enrollmentDTO) {
        return ResponseEntity.ok(studentService.enrollInCareer(enrollmentDTO));
    }

    @PostMapping("/enroll-group")
    public ResponseEntity<StudentResponseDTO> enrollInGroup(@RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.ok(studentService.enrollInGroup(enrollmentDTO));
    }

    @GetMapping("/{id}/academic-record")
    public ResponseEntity<AcademicRecordDTO> getAcademicRecord(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getAcademicRecord(id));
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<ScheduleStudentDTO> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getSchedule(id));
    }

    @PostMapping("/cancel-enrollment")
    public ResponseEntity<StudentResponseDTO> cancelGroupEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        return ResponseEntity.ok(studentService.cancelGroupEnrollment(
                enrollmentDTO.getStudentId(),
                enrollmentDTO.getGroupId()));
    }

    @PostMapping("/{studentId}/periods/initialize")
    public ResponseEntity<Void> initializeStudentPeriods(@PathVariable Long studentId) {
        studentService.initializeStudentPeriods(studentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{studentId}/periods")
    public ResponseEntity<List<StudentSemesterPeriodsDTO>> getStudentPeriods(@PathVariable Long studentId) {
        List<StudentSemesterPeriodsDTO> periods = studentService.getStudentPeriods(studentId);
        return ResponseEntity.ok(periods);
    }


}