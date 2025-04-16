package co.edu.udes.backend.mappers.career;

import co.edu.udes.backend.dto.career.CareerDTO;
import co.edu.udes.backend.models.Career;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CareerMapper {

    Career toEntity (CareerDTO careerDTO);

    CareerDTO toDto (Career career);
}
