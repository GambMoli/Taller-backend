package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.evaluation.EvaluationDTO;
import co.edu.udes.backend.dto.evaluation.EvaluationResponse;
import co.edu.udes.backend.service.EvaluationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EvaluationResponse> create(
            @ModelAttribute EvaluationDTO evaluationDTO) throws IOException {
        EvaluationResponse response = evaluationService.createEvaluation(evaluationDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<EvaluationResponse>> findAll() {
        return ResponseEntity.ok(evaluationService.getEvaluations());
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getEvaluationFile(@PathVariable Long id) {
        try {
            byte[] fileData = evaluationService.getEvaluationFile(id);

            String fileName = "evaluation-file";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName) // Forzar descarga del archivo
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


}
