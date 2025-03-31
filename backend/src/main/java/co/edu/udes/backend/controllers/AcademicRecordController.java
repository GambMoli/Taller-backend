package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.AcademicRecord;
import co.edu.udes.backend.models.Course;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.repositories.AcademicRecordRepository;
import co.edu.udes.backend.repositories.CourseRepository;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/academic-records")
public class AcademicRecordController {

    @Autowired
    private AcademicRecordRepository academicRecordRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Obtener el historial académico de un estudiante
    @GetMapping("/{studentId}")
    public ResponseEntity<AcademicRecord> getAcademicRecord(@PathVariable Long studentId) {
        AcademicRecord academicRecord = academicRecordRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found for student with id: " + studentId));
        return ResponseEntity.ok(academicRecord);
    }

    // Agregar un curso completado al historial académico
    @PostMapping("/{studentId}/completed-courses")
    public ResponseEntity<AcademicRecord> addCompletedCourse(@PathVariable Long studentId,
                                                             @RequestParam Long courseId,
                                                             @RequestParam Double grade) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        // Buscar o crear el registro académico del estudiante
        AcademicRecord academicRecord = academicRecordRepository.findByStudentId(studentId)
                .orElse(new AcademicRecord(student));

        academicRecord.addCompletedCourse(course, grade);

        academicRecordRepository.save(academicRecord);
        return ResponseEntity.ok(academicRecord);
    }

    // Obtener todos los cursos completados de un estudiante
    @GetMapping("/{studentId}/completed-courses")
    public ResponseEntity<List<Course>> getCompletedCourses(@PathVariable Long studentId) {
        AcademicRecord academicRecord = academicRecordRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found for student with id: " + studentId));

        List<Course> completedCourses = (List<Course>) academicRecord.getCompletedCourses();
        return ResponseEntity.ok(completedCourses);
    }

    // Obtener los cursos actuales en los que el estudiante está inscrito
    @GetMapping("/{studentId}/current-courses")
    public ResponseEntity<List<Course>> getCurrentCourses(@PathVariable Long studentId) {
        AcademicRecord academicRecord = academicRecordRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found for student with id: " + studentId));

        List<Course> currentCourses = academicRecord.getCurrentCourses();
        return ResponseEntity.ok(currentCourses);
    }

    // Obtener el total de créditos completados por el estudiante
    @GetMapping("/{studentId}/total-credits")
    public ResponseEntity<Integer> getTotalCredits(@PathVariable Long studentId) {
        AcademicRecord academicRecord = academicRecordRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found for student with id: " + studentId));

        Integer totalCredits = academicRecord.getTotalCredits();
        return ResponseEntity.ok(totalCredits);
    }

    // Calcular el GPA del estudiante
    @GetMapping("/{studentId}/gpa")
    public ResponseEntity<Double> calculateGPA(@PathVariable Long studentId) {
        AcademicRecord academicRecord = academicRecordRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found for student with id: " + studentId));

        Double gpa = academicRecord.calculateGPA();
        return ResponseEntity.ok(gpa);
    }

    // Obtener el estado académico del estudiante
    @GetMapping("/{studentId}/academic-status")
    public ResponseEntity<String> getAcademicStatus(@PathVariable Long studentId) {
        AcademicRecord academicRecord = academicRecordRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic record not found for student with id: " + studentId));

        String academicStatus = academicRecord.getAcademicStatus();
        return ResponseEntity.ok(academicStatus);
    }
}
