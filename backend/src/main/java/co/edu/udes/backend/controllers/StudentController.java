package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.AcademicRecord;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

//    @PostMapping
//    public Student create(@RequestBody Student student, @RequestParam Long careerId) {
//        return studentService.create(student, careerId);
//    }
//
//    @PutMapping("/{id}")
//    public Student update(@PathVariable Long id, @RequestBody Student student, @RequestParam Long careerId) {
//        return studentService.update(id, student, careerId);
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }

    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable Long id) {
        return studentService.findById(id);
    }

//    @PostMapping("/{studentId}/enroll")
//    public List<AcademicRecord> enroll(@PathVariable Long studentId, @RequestBody List<Long> groupIds) {
//        return studentService.enrollSubjects(studentId, groupIds);
//    }

    @GetMapping("/{studentId}/current-subjects")
    public List<AcademicRecord> getCurrentSubjects(@PathVariable Long studentId) {
        return studentService.getCurrentSubjects(studentId);
    }

    @GetMapping("/{studentId}/records")
    public List<AcademicRecord> getAllRecords(@PathVariable Long studentId) {
        return studentService.getAllRecords(studentId);
    }
}
