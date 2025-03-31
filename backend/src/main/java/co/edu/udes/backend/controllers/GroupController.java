package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Course;
import co.edu.udes.backend.models.Enrollment;
import co.edu.udes.backend.models.Group;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Professor;
import co.edu.udes.backend.repositories.CourseRepository;
import co.edu.udes.backend.repositories.GroupRepository;
import co.edu.udes.backend.repositories.ProfessorRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @GetMapping
    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));
        return ResponseEntity.ok(group);
    }

    @PostMapping
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        // Verificar que el curso existe
        if (group.getCourse() != null && group.getCourse().getId() != null) {
            Course course = courseRepository.findById(group.getCourse().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
            group.setCourse(course);
        }

        // Verificar que el profesor existe
        if (group.getProfessor() != null && group.getProfessor().getId() != null) {
            Professor professor = professorRepository.findById(group.getProfessor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
            group.setProfessor(professor);
        }

        Group newGroup = groupRepository.save(group);
        return ResponseEntity.ok(newGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long id, @RequestBody Group groupDetails) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));

        // No permitimos cambiar el curso una vez creado el grupo
        if (groupDetails.getProfessor() != null && groupDetails.getProfessor().getId() != null) {
            Professor professor = professorRepository.findById(groupDetails.getProfessor().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Professor not found"));
            group.setProfessor(professor);
        }

        if (groupDetails.getSchedule() != null) {
            group.setSchedule(groupDetails.getSchedule());
        }

        Group updatedGroup = groupRepository.save(group);
        return ResponseEntity.ok(updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));

        // Verificar si hay estudiantes inscritos antes de eliminar
        if (group.getStudents() != null && !group.getStudents().isEmpty()) {
            return ResponseEntity.badRequest().body("Cannot delete group with enrolled students");
        }

        groupRepository.delete(group);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getGroupStudents(@PathVariable Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + id));

        return ResponseEntity.ok(group.getStudents());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Group>> getGroupsByCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        List<Group> groups = groupRepository.findByCourse(course);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/professor/{professorId}")
    public ResponseEntity<List<Group>> getGroupsByProfessor(@PathVariable Long professorId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + professorId));

        List<Group> groups = groupRepository.findByProfessor(professor);
        return ResponseEntity.ok(groups);
    }
}