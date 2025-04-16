package co.edu.udes.backend.mappers.teacher;


import co.edu.udes.backend.dto.teacher.TeacherDTO;
import co.edu.udes.backend.dto.teacher.TeacherResponseDTO;
import co.edu.udes.backend.dto.teacher.TeacherScheduleDTO;
import co.edu.udes.backend.models.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    Teacher toEntity(TeacherDTO dto);

    TeacherDTO toDto(Teacher teacher);

    TeacherResponseDTO toResponseDto(Teacher teacher);

    TeacherScheduleDTO toScheduleDto(Teacher teacher);
}
