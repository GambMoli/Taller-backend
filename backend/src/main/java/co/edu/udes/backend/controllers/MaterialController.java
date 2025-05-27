package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.material.MaterialDTO;
import co.edu.udes.backend.dto.material.MaterialResponseDTO;
import co.edu.udes.backend.service.MaterialService;
import co.edu.udes.backend.service.ReserveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService){
        this.materialService= materialService;
    }

    @GetMapping
    public ResponseEntity<List<MaterialResponseDTO>> getAlls(){
        return ResponseEntity.ok(materialService.getAlls());
    }

    @PostMapping
    public ResponseEntity<MaterialResponseDTO> createMaterial(@RequestBody  MaterialDTO materialDTO){
        return ResponseEntity.ok(materialService.createMaterial(materialDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> modifyMaterial(@PathVariable long id ,@RequestBody MaterialDTO materialDTO){
        return ResponseEntity.ok(materialService.modifyMaterial(id, materialDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MaterialResponseDTO> deleteMaterial (@PathVariable long id){
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
