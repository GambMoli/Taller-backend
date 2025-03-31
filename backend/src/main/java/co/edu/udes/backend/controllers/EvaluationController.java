package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Evaluation;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Calification;
import co.edu.udes.backend.repositories.EvaluationRepository;
import co.edu.udes.backend.repositories.CalificationRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private CalificationRepository calificationRepository;

    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(@RequestBody Evaluation evaluation) {
        Evaluation newEvaluation = evaluationRepository.save(evaluation);
        return ResponseEntity.ok(newEvaluation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(@PathVariable Long id, @RequestBody Evaluation evaluationDetails) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + id));

        evaluation.setTitle(evaluationDetails.getTitle());
        evaluation.setDescription(evaluationDetails.getDescription());
        evaluation.setFeedback(evaluationDetails.getFeedback());

        Evaluation updatedEvaluation = evaluationRepository.save(evaluation);
        return ResponseEntity.ok(updatedEvaluation);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + id));
        return ResponseEntity.ok(evaluation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvaluation(@PathVariable Long id) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + id));

        evaluationRepository.delete(evaluation);
        return ResponseEntity.noContent().build();
    }

    // Asignar calificación a los estudiantes
    @PostMapping("/{evaluationId}/califications")
    public ResponseEntity<Calification> assignCalification(@PathVariable Long evaluationId, @RequestBody Calification calification) {
        Evaluation evaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation not found with id: " + evaluationId));

        calification.setEvaluation(evaluation); // Asignamos la evaluación a la calificación

        Calification newCalification = calificationRepository.save(calification);
        return ResponseEntity.ok(newCalification);
    }

    @PutMapping("/{evaluationId}/califications/{calificationId}")
    public ResponseEntity<Calification> updateCalification(@PathVariable Long evaluationId, @PathVariable Long calificationId, @RequestBody Calification calificationDetails) {
        Calification calification = calificationRepository.findById(calificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Calification not found with id: " + calificationId));

        calification.setScore(calificationDetails.getScore());
        calification.setComments(calificationDetails.getComments());

        Calification updatedCalification = calificationRepository.save(calification);
        return ResponseEntity.ok(updatedCalification);
    }
}
