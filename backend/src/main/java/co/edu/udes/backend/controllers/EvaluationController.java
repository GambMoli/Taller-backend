package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.evaluation.EvaluationDTO;
import co.edu.udes.backend.dto.evaluation.EvaluationResponse;
import co.edu.udes.backend.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {
    private EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping
    public ResponseEntity<EvaluationResponse> create(
            @ModelAttribute EvaluationDTO evaluationDTO) throws IOException {
        EvaluationResponse response = evaluationService.createEvaluation(evaluationDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EvaluationResponse>> findAll() {
        return ResponseEntity.ok(evaluationService.getEvaluations());
    }

}
