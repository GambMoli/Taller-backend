package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Professor;
import co.edu.udes.backend.models.Group;
import co.edu.udes.backend.repositories.ProfessorRepository;
import co.edu.udes.backend.repositories.GroupRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professors")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private GroupRepository groupRepository;

    @GetMapping
    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> getProfessorById(@PathVariable Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + id));
        return ResponseEntity.ok(professor);
    }

    @PostMapping
    public Professor createProfessor(@RequestBody Professor professor) {
        return professorRepository.save(professor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(@PathVariable Long id, @RequestBody Professor professorDetails) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + id));

        professor.setDepartment(professorDetails.getDepartment());
        // TO DO: Actualizar otros atributos heredados de User

        Professor updatedProfessor = professorRepository.save(professor);
        return ResponseEntity.ok(updatedProfessor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfessor(@PathVariable Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + id));

        // Verificar si el profesor tiene grupos asignados antes de eliminar
        List<Group> assignedGroups = groupRepository.findByProfessor(professor);
        if (!assignedGroups.isEmpty()) {
            return ResponseEntity.badRequest().body("Cannot delete professor with assigned groups");
        }

        professorRepository.delete(professor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/groups")
    public ResponseEntity<List<Group>> getProfessorGroups(@PathVariable Long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + id));

        List<Group> groups = groupRepository.findByProfessor(professor);
        return ResponseEntity.ok(groups);
    }
}
