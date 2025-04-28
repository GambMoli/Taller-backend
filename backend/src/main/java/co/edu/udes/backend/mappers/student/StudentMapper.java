package co.edu.udes.backend.mappers.student;

import co.edu.udes.backend.dto.student.StudentDTO;
import co.edu.udes.backend.dto.student.StudentResponseDTO;
import co.edu.udes.backend.models.Student;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    Student toEntity(StudentDTO dto);

    StudentDTO toDTO(Student student);

    @Mapping(source = "career.id", target = "careerId")
    @Mapping(source = "career.name", target = "careerName")
    StudentResponseDTO toResponseDTO(Student student);

    @InheritInverseConfiguration(name = "toResponseDTO")
    Student fromResponseDTO(StudentResponseDTO dto);
}
