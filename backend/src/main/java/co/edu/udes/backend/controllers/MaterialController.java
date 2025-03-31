package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Material;
import co.edu.udes.backend.repositories.MaterialRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/materials")
public class MaterialController {

    @Autowired
    private MaterialRepository materialRepository;

    // Prestar material
    @PutMapping("/{id}/borrow")
    public ResponseEntity<Material> borrowMaterial(@PathVariable Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));

        material.borrowMaterial();
        materialRepository.save(material);
        return ResponseEntity.ok(material);
    }

    // Devolver material
    @PutMapping("/{id}/return")
    public ResponseEntity<Material> returnMaterial(@PathVariable Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));

        material.returnMaterial();
        materialRepository.save(material);
        return ResponseEntity.ok(material);
    }

    // Verificar disponibilidad de material
    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found with id: " + id));

        boolean isAvailable = material.checkAvailability();
        return ResponseEntity.ok(isAvailable);
    }
}
