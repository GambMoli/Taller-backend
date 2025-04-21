package co.edu.udes.backend.mappers.attendance;

import co.edu.udes.backend.dto.attendance.AttendanceCreateDTO;
import co.edu.udes.backend.dto.attendance.AttendanceDTO;
import co.edu.udes.backend.dto.attendance.AttendanceResponseDTO;
import co.edu.udes.backend.models.Attendance;
import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Period;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.name", target = "studentName")
    @Mapping(source = "group.id", target = "groupId")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "period.id", target = "periodId")
    @Mapping(source = "period.name", target = "periodName")
    AttendanceDTO toDTO(Attendance attendance);

    @Mapping(source = "student.name", target = "studentName")
    @Mapping(source = "group.name", target = "groupName")
    @Mapping(source = "period.name", target = "periodName")
    AttendanceResponseDTO toResponseDTO(Attendance attendance);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", source = "student")
    @Mapping(target = "group", source = "group")
    @Mapping(target = "period", source = "period")
    @Mapping(target = "registeredBy", source = "teacher")
    @Mapping(target = "registeredDate", expression = "java(java.time.LocalDate.now())")
    Attendance toEntity(AttendanceCreateDTO dto, Student student, GroupClass group, Period period, Teacher teacher);

    void updateAttendanceFromDTO(AttendanceDTO dto, @MappingTarget Attendance attendance);
}