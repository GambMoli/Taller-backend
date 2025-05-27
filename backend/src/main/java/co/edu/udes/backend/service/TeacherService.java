package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.dto.schedule.ClassScheduleDTO;
import co.edu.udes.backend.dto.schedule.DayScheduleDTO;
import co.edu.udes.backend.dto.teacher.*;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.reserve.ReserveMapper;
import co.edu.udes.backend.mappers.teacher.TeacherMapper;
import co.edu.udes.backend.models.*;
import co.edu.udes.backend.repositories.GroupClassRepository;
import co.edu.udes.backend.repositories.ReserveRepository;
import co.edu.udes.backend.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final GroupClassRepository groupClassRepository;
    private final TeacherMapper teacherMapper;
    private final ReserveRepository reserveRepository;
    private final ReserveMapper reserveMapper;

    public TeacherService(TeacherRepository teacherRepository, GroupClassRepository groupClassRepository, TeacherMapper teacherMapper, ReserveRepository reserveRepository, ReserveMapper reserveMapper) {
        this.teacherRepository = teacherRepository;
        this.groupClassRepository = groupClassRepository;
        this.teacherMapper = teacherMapper;
        this.reserveMapper= reserveMapper;
        this.reserveRepository=reserveRepository;
    }

    @Autowired
    private RoleService roleService;

    public TeacherResponseDTO create(TeacherDTO teacherDTO) {
        if (teacherRepository.existsByEmail(teacherDTO.getEmail())) {
            throw new CustomException(ErrorCode.TEACHER_ALREADY_EXISTS);
        }

        Teacher teacher = teacherMapper.toEntity(teacherDTO);


        teacher.setRole(roleService.getRoleByName(RoleService.ROLE_TEACHER));

        Teacher savedTeacher = teacherRepository.save(teacher);
        return teacherMapper.toResponseDto(savedTeacher);
    }

    public List<TeacherResponseDTO> findAll() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper :: toResponseDto)
                .collect(Collectors.toList());
    }

    public TeacherResponseDTO findById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));
        return teacherMapper.toResponseDto(teacher);
    }

    public TeacherResponseDTO update(Long id, TeacherDTO teacherDTO) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));

        if (!existing.getEmail().equals(teacherDTO.getEmail()) &&
                teacherRepository.existsByEmail(teacherDTO.getEmail())) {
            throw new CustomException(ErrorCode.TEACHER_ALREADY_EXISTS);
        }

        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        Teacher updatedTeacher = teacherRepository.save(teacher);

        return teacherMapper.toResponseDto(updatedTeacher);
    }

    public void delete(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new CustomException(ErrorCode.TEACHER_NOT_FOUND);
        }
        teacherRepository.deleteById(id);
    }

    private boolean isOverlapping(String start1, String end1, String start2, String end2) {
        int s1 = toMinutes(start1);
        int e1 = toMinutes(end1);
        int s2 = toMinutes(start2);
        int e2 = toMinutes(end2);

        return s1 < e2 && s2 < e1;
    }

    private int toMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    @Transactional
    public TeacherResponseDTO assignGroup(GroupAssignmentDTO assignmentDTO) {
        Teacher teacher = teacherRepository.findById(assignmentDTO.getTeacherId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));

        GroupClass group = groupClassRepository.findById(assignmentDTO.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        if (group.getSchedules() == null || group.getSchedules().isEmpty()) {
            throw new CustomException(ErrorCode.GROUP_WITHOUT_SCHEDULE);
        }

        if (group.getTeacher() != null && !group.getTeacher().getId().equals(teacher.getId())) {
            throw new CustomException(ErrorCode.GROUP_ALREADY_ASSIGNED_TO_ANOTHER_TEACHER);
        }

        // Obtener todos los horarios del profesor
        List<Schedule> teacherSchedules = new ArrayList<>();
        for (GroupClass assignedGroup : teacher.getAssignedGroups()) {
            teacherSchedules.addAll(assignedGroup.getSchedules());
        }

        // Verificar choques de horario
        for (Schedule newSchedule : group.getSchedules()) {
            for (Schedule existing : teacherSchedules) {
                if (newSchedule.getDay().equals(existing.getDay()) &&
                        isOverlapping(newSchedule.getStartTime(), newSchedule.getEndTime(),
                                existing.getStartTime(), existing.getEndTime())) {
                    throw new CustomException(ErrorCode.TEACHER_SCHEDULE_CONFLICT);
                }
            }
        }

        // Calcular carga horaria
        int currentAssignedHours = teacher.getAssignedGroups().stream()
                .mapToInt(GroupClass::getTotalHours)
                .sum();

        int groupHours = group.getTotalHours();

        if (currentAssignedHours + groupHours > teacher.getWorkloadHours()) {
            throw new CustomException(ErrorCode.TEACHER_WORKLOAD_EXCEEDED);
        }

        if (group.getTeacher() == null) {
            group.setTeacher(teacher);
            teacher.getAssignedGroups().add(group);
        }

        groupClassRepository.save(group);
        Teacher updatedTeacher = teacherRepository.save(teacher);

        return teacherMapper.toResponseDto(updatedTeacher);
    }


    @Transactional(readOnly = true)
    public TeacherScheduleDTO getSchedule(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));

        // Calcular horas asignadas totales
        int assignedHours = teacher.getAssignedGroups().stream()
                .mapToInt(GroupClass::getTotalHours)
                .sum();

        // Crear DTO básico del profesor
        co.edu.udes.backend.dto.teacher.TeacherScheduleDTO scheduleDTO = teacherMapper.toScheduleDto(teacher);
        scheduleDTO.setAssignedHours(assignedHours);

        // Crear un array para los 7 días de la semana (0=Lunes, 1=Martes, ..., 6=Domingo)
        List<DayScheduleDTO> weekSchedule = new ArrayList<>();

        // Inicializar los días de la semana
        String[] dayNames = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (int i = 0; i < dayNames.length; i++) {
            DayScheduleDTO daySchedule = new DayScheduleDTO();
            daySchedule.setDayName(dayNames[i]);
            daySchedule.setClasses(new ArrayList<>());
            weekSchedule.add(daySchedule);
        }

        // Recorrer los grupos y horarios del profesor
        for (GroupClass group : teacher.getAssignedGroups()) {
            for (Schedule schedule : group.getSchedules()) {
                // Calcular la duración de la clase en horas
                String[] start = schedule.getStartTime().split(":");
                String[] end = schedule.getEndTime().split(":");

                int startHour = Integer.parseInt(start[0]);
                int startMinute = Integer.parseInt(start[1]);
                int endHour = Integer.parseInt(end[0]);
                int endMinute = Integer.parseInt(end[1]);

                int durationMinutes = (endHour * 60 + endMinute) - (startHour * 60 + startMinute);
                int hours = durationMinutes / 60;

                // Crear el DTO para cada clase
                ClassScheduleDTO classSchedule = new ClassScheduleDTO(
                        group.getId(),
                        group.getSubject().getName(),
                        schedule.getDay(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        schedule.getClassroom()
                );

                // Determinar el índice del día según su nombre
                int dayIndex = getDayIndex(schedule.getDay());

                // Si el día es válido, agregar la clase al día correspondiente
                if (dayIndex >= 0 && dayIndex < weekSchedule.size()) {
                    weekSchedule.get(dayIndex).getClasses().add(classSchedule);
                }
            }
        }

        scheduleDTO.setWeekSchedule(weekSchedule);
        return scheduleDTO;
    }

    public List<ReserveResponseDTO> getReservesByTeacher(Long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        List<Reserve> reserves = reserveRepository.findByTeacher(teacher);
        return reserves.stream()
                .map(reserveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    private int getDayIndex(String dayName) {
        switch (dayName.toLowerCase()) {
            case "lunes": return 0;
            case "martes": return 1;
            case "miércoles":
            case "miercoles": return 2;
            case "jueves": return 3;
            case "viernes": return 4;
            case "sábado":
            case "sabado": return 5;
            case "domingo": return 6;
            default: return -1; // Día inválido
        }
    }
}