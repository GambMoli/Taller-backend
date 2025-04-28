package co.edu.udes.backend.mappers.place;

import co.edu.udes.backend.dto.place.PlaceDTO;
import co.edu.udes.backend.dto.place.PlaceResponseDTO;
import co.edu.udes.backend.models.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaceMapper {
    Place toEntity(PlaceDTO placeDTO);
    PlaceResponseDTO toResponseDTO(Place place);
}
