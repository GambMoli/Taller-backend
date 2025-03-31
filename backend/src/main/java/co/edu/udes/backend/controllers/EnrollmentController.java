// EnrollmentController.java
package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Course;
import co.edu.udes.backend.models.Enrollment;
import co.edu.udes.backend.models.Group;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.repositories.CourseRepository;
import co.edu.udes.backend.repositories.EnrollmentRepository;
import co.edu.udes.backend.repositories.GroupRepository;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Obtener todas las inscripciones
    @GetMapping
    public List<Enrollment> getEnrollments() {
        return enrollmentRepository.findAll();
    }

    // Obtener inscripción por ID
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        Enrollment enrollmentDetail = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment doesn't exist with this id: " + id));

        return ResponseEntity.ok(enrollmentDetail);
    }

    // Inscribir un estudiante a un grupo
    @PostMapping("/student/{studentId}/group/{groupId}")
    public ResponseEntity<?> enrollStudentToGroup(
            @PathVariable Long studentId,
            @PathVariable Long groupId,
            @RequestBody Map<String, String> enrollmentData) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));

        // Verificar si el grupo tiene capacidad
        if (group.getStudents() != null && group.getStudents().size() >= group.getCourse().getCapacity()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Group has reached maximum capacity");
        }

        // Verificar prerrequisitos
        List<Course> prerequisites = group.getCourse().getPreRequisitesCourses();
        if (prerequisites != null && !prerequisites.isEmpty()) {
            // Aquí iría la lógica para verificar si el estudiante ha aprobado los prerrequisitos
            // Esta es una implementación simplificada
            boolean hasCompletedPrerequisites = true; // TO DO LOGICA REAL

            if (!hasCompletedPrerequisites) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Student has not completed prerequisites for this course");
            }
        }

        // Crear la inscripción
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setPeriod(enrollmentData.get("period"));
        enrollment.setStatus("ACTIVE");

        // Guardar la inscripción
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // Agregar el estudiante al grupo
        group.addStudent(student);
        groupRepository.save(group);

        return ResponseEntity.ok(savedEnrollment);
    }

    // Editar inscripción
    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> editEnrollment(@PathVariable Long id, @RequestBody Enrollment enrollmentBody) {
        Enrollment enrollmentDetail = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment doesn't exist with this id: " + id));

        enrollmentDetail.setPeriod(enrollmentBody.getPeriod());
        enrollmentDetail.setStatus(enrollmentBody.getStatus());
        // No permitimos cambiar el estudiante una vez creada la inscripción

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollmentDetail);
        return ResponseEntity.ok(updatedEnrollment);
    }

    // Cancelar inscripción
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelEnrollment(@PathVariable Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment doesn't exist with this id: " + id));

        // Cambiar estado de la inscripción
        enrollment.setStatus("CANCELLED");
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);

        return ResponseEntity.ok(updatedEnrollment);
    }

    // Obtener inscripciones por estudiante
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        return ResponseEntity.ok(enrollments);
    }
}