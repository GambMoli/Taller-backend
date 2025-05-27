package co.edu.udes.backend.mappers.reserve;

import co.edu.udes.backend.dto.reserve.ReserveDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.models.Place;
import co.edu.udes.backend.models.Reserve;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface ReserveMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "reserveDTO.code")
    @Mapping(target = "place", source = "place")
    @Mapping(target = "student", source = "student")
    @Mapping(target = "teacher", source = "teacher")
    Reserve toEntity(ReserveDTO reserveDTO, Place place, Student student, Teacher teacher);

    ReserveDTO toDTO(Reserve reserve);
    ReserveResponseDTO toResponseDTO(Reserve reserve);


}
