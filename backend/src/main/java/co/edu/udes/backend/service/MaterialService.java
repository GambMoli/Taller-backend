package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.material.MaterialDTO;
import co.edu.udes.backend.dto.material.MaterialResponseDTO;

import co.edu.udes.backend.mappers.material.MaterialMapper;
import co.edu.udes.backend.models.Material;

import co.edu.udes.backend.repositories.MaterialRepository;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final MaterialMapper materialMapper;

    public MaterialService(MaterialMapper materialMapper, MaterialRepository materialRepository){
        this.materialMapper=materialMapper;
        this.materialRepository=materialRepository;
    }

    public List<MaterialResponseDTO> getAlls(){
        return  materialRepository.findAll().stream()
                .map(materialMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public MaterialResponseDTO getOne(long id){
        Material material = materialRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("NO EXISTE EL MATERIAL CON ESE ID" + id));

        return materialMapper.toResponseDTO(material);
    }

    public MaterialResponseDTO createMaterial(MaterialDTO materialDTO) {

        if (materialDTO.getCode() == null || materialDTO.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("El código de material no puede ser nulo o vacío");
        }

        if (materialRepository.existsByCode(materialDTO.getCode())) {
            throw new RuntimeException("Ya existe el material");
        }

        Material material = materialMapper.toEntity(materialDTO);

        // Double check after mapping
        if (material.getCode() == null || material.getCode().trim().isEmpty()) {
            throw new IllegalArgumentException("Error en el mapeo: El código de material se convirtió a nulo");
        }

        Material materialSave = materialRepository.save(material);
        return materialMapper.toResponseDTO(materialSave);
    }

    public MaterialResponseDTO modifyMaterial(long id, MaterialDTO materialDTO) {

        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el material con ese ID"));


        if (materialDTO.getCode() != null && materialDTO.getCode().equals(existingMaterial.getCode())) {
            throw new IllegalArgumentException("El código nuevo no puede ser igual al código actual");
        }


        existingMaterial.setName(materialDTO.getName());
        existingMaterial.setCode(materialDTO.getCode());
        existingMaterial.setDescription(materialDTO.getDescription());
        existingMaterial.setType(materialDTO.getType());
        existingMaterial.setState(materialDTO.getState());
        existingMaterial.setStock(materialDTO.getStock());
        existingMaterial.setEntryDate(materialDTO.getEntryDate());

        Material materialModified = materialRepository.save(existingMaterial);

        return materialMapper.toResponseDTO(materialModified);
    }

    public void deleteMaterial(long id){
        materialRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("No existe el material con ese ID"));

        materialRepository.deleteById(id);
    }
}
