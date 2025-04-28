package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.material.MaterialDTO;
import co.edu.udes.backend.dto.material.MaterialResponseDTO;

import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
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
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MATERIAL));

        return materialMapper.toResponseDTO(material);
    }

    public MaterialResponseDTO createMaterial(MaterialDTO materialDTO) {

        if (materialDTO.getCode() == null || materialDTO.getCode().trim().isEmpty()) {
            throw new CustomException(ErrorCode.CODE_IS_NULL);
        }

        if (materialRepository.existsByCode(materialDTO.getCode())) {
            throw new CustomException(ErrorCode.MATERIAL_EXISTS);
        }

        Material material = materialMapper.toEntity(materialDTO);

        // Double check after mapping
        if (material.getCode() == null || material.getCode().trim().isEmpty()) {
            throw new CustomException(ErrorCode.CODE_CONVERTD_IN_NULL);
        }

        Material materialSave = materialRepository.save(material);
        return materialMapper.toResponseDTO(materialSave);
    }

    public MaterialResponseDTO modifyMaterial(long id, MaterialDTO materialDTO) {

        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MATERIAL));


        if (materialDTO.getCode() != null && materialDTO.getCode().equals(existingMaterial.getCode())) {
            throw new CustomException(ErrorCode.CODE_IS_SAME);
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
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_MATERIAL));

        materialRepository.deleteById(id);
    }
}
