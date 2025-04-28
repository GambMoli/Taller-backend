package co.edu.udes.backend.mappers.material;

import co.edu.udes.backend.dto.material.MaterialDTO;
import co.edu.udes.backend.dto.material.MaterialResponseDTO;
import co.edu.udes.backend.models.Material;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    @Mapping(source = "code" , target = "code")
    Material toEntity (MaterialDTO materialDTO);


    MaterialResponseDTO toResponseDTO(Material material);
}
