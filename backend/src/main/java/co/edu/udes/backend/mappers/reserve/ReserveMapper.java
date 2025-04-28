package co.edu.udes.backend.mappers.reserve;

import co.edu.udes.backend.dto.reserve.ReserveDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.models.Reserve;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReserveMapper {
    Reserve toEntity (ReserveDTO reserveDTO);
    ReserveDTO toDTO(Reserve reserve);
    ReserveResponseDTO toResponseDTO(Reserve reserve);
}
