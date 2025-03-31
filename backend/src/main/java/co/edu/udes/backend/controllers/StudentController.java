package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        student.setCode(studentDetails.getCode());
        student.setEnrollmentStatus(studentDetails.getEnrollmentStatus());
        student.setEmail(studentDetails.getEmail());
        student.setName(studentDetails.getName());
        student.setPassword(studentDetails.getPassword());

        Student updatedStudent = studentRepository.save(student);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // TO DO: Verificar si el estudiante tiene inscripciones activas antes de eliminar

        studentRepository.delete(student);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/manage-enrollments")
    public ResponseEntity<?> manageEnrollments(@PathVariable Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

        // TODO: Esta llamada retornaría la vista o datos necesarios para gestionar las inscripciones
        // de un estudiante específico

        return ResponseEntity.ok().build();
    }
}
