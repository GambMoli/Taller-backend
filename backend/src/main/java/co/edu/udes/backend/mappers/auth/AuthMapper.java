package co.edu.udes.backend.mappers.auth;


import co.edu.udes.backend.dto.login.JwtResponseDTO;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "token", source = "token")
    @Mapping(target = "id", source = "teacher.id")
    @Mapping(target = "email", source = "teacher.email")
    @Mapping(target = "name", source = "teacher.name")
    @Mapping(target = "role", source = "teacher.role")
    JwtResponseDTO teacherToJwtResponseDTO(Teacher teacher, String token);

    @Mapping(target = "token", source = "token")
    @Mapping(target = "id", source = "student.id")
    @Mapping(target = "email", source = "student.email")
    @Mapping(target = "name", source = "student.name")
    @Mapping(target = "role", source = "student.role")
    JwtResponseDTO studentToJwtResponseDTO(Student student, String token);
}
