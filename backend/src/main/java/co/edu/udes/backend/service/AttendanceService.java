package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.attendance.*;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.attendance.AttendanceMapper;
import co.edu.udes.backend.models.*;
import co.edu.udes.backend.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final GroupClassRepository groupClassRepository;
    private final PeriodRepository periodRepository;
    private final TeacherRepository teacherRepository;
    private final AttendanceMapper attendanceMapper;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            StudentRepository studentRepository,
            GroupClassRepository groupClassRepository,
            PeriodRepository periodRepository,
            TeacherRepository teacherRepository,
            AttendanceMapper attendanceMapper) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.groupClassRepository = groupClassRepository;
        this.periodRepository = periodRepository;
        this.teacherRepository = teacherRepository;
        this.attendanceMapper = attendanceMapper;
    }

    @Transactional
    public AttendanceResponseDTO markAttendance(AttendanceCreateDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        GroupClass group = groupClassRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Period period = periodRepository.findById(dto.getPeriodId())
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        Teacher teacher = teacherRepository.findById(dto.getRegisteredById())
                .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));

        // Verify student is enrolled in this group
        if (!student.getEnrolledGroups().contains(group)) {
            throw new CustomException(ErrorCode.STUDENT_NOT_IN_GROUP);
        }

        // Verify class date is within period dates
        if (period.getStartDate() == null || period.getEndDate() == null) {
            throw new CustomException(ErrorCode.PERIOD_DATES_MISSING);
        }

        LocalDate classDate = dto.getClassDate();
        if (classDate.isBefore(period.getStartDate()) || classDate.isAfter(period.getEndDate())) {
            throw new CustomException(ErrorCode.INVALID_CLASS_DATE);
        }

        // Verify class date is a valid class day based on schedule
        boolean isValidClassDay = isScheduledClassDay(group, classDate);
        if (!isValidClassDay) {
            throw new CustomException(ErrorCode.INVALID_CLASS_DATE);
        }

        // Check if attendance already exists
        Optional<Attendance> existingAttendance = attendanceRepository
                .findByStudentIdAndGroupIdAndClassDate(student.getId(), group.getId(), classDate);

        Attendance attendance;
        if (existingAttendance.isPresent()) {
            // Update existing attendance
            attendance = existingAttendance.get();
            attendance.setPresent(dto.getPresent());
            attendance.setRegisteredBy(teacher);
            attendance.setRegisteredDate(LocalDate.now());
        } else {
            // Create new attendance
            attendance = attendanceMapper.toEntity(dto, student, group, period, teacher);
        }

        Attendance savedAttendance = attendanceRepository.save(attendance);
        return attendanceMapper.toResponseDTO(savedAttendance);
    }

    @Transactional
    public List<AttendanceResponseDTO> markBulkAttendance(BulkAttendanceDTO dto) {
        if (dto.getStudentIds() == null || dto.getStudentIds().isEmpty()) {
            throw new CustomException(ErrorCode.EMPTY_STUDENT_LIST);
        }

        GroupClass group = groupClassRepository.findById(dto.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Period period = periodRepository.findById(dto.getPeriodId())
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        Teacher teacher = teacherRepository.findById(dto.getRegisteredById())
                .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));

        // Verify class date is within period dates
        if (period.getStartDate() == null || period.getEndDate() == null) {
            throw new CustomException(ErrorCode.PERIOD_DATES_MISSING);
        }

        LocalDate classDate = dto.getClassDate();
        if (classDate.isBefore(period.getStartDate()) || classDate.isAfter(period.getEndDate())) {
            throw new CustomException(ErrorCode.INVALID_CLASS_DATE);
        }

        // Verify class date is a valid class day based on schedule
        boolean isValidClassDay = isScheduledClassDay(group, classDate);
        if (!isValidClassDay) {
            throw new CustomException(ErrorCode.INVALID_CLASS_DATE);
        }

        List<AttendanceResponseDTO> results = new ArrayList<>();

        for (Long studentId : dto.getStudentIds()) {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

            // Verify student is enrolled in this group
            if (!student.getEnrolledGroups().contains(group)) {
                continue; // Skip this student but don't fail the whole operation
            }

            // Create attendance DTO for this student
            AttendanceCreateDTO attendanceDTO = new AttendanceCreateDTO(
                    studentId,
                    dto.getGroupId(),
                    dto.getClassDate(),
                    dto.getPresent(),
                    dto.getPeriodId(),
                    dto.getRegisteredById()
            );

            // Mark attendance for this student
            AttendanceResponseDTO response = markAttendance(attendanceDTO);
            results.add(response);
        }

        return results;
    }

    @Transactional(readOnly = true)
    public ClassDatesDTO getClassDatesForGroupAndPeriod(Long groupId, Long periodId) {
        GroupClass group = groupClassRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        if (period.getStartDate() == null || period.getEndDate() == null) {
            throw new CustomException(ErrorCode.PERIOD_DATES_MISSING);
        }

        List<LocalDate> classDates = calculateClassDates(group, period.getStartDate(), period.getEndDate());

        if (classDates.isEmpty()) {
            throw new CustomException(ErrorCode.NO_CLASSES_IN_PERIOD);
        }

        ClassDatesDTO response = new ClassDatesDTO();
        response.setGroupId(group.getId());
        response.setGroupName(group.getName());
        response.setSubjectName(group.getSubject().getName());
        response.setPeriodId(period.getId());
        response.setPeriodName(period.getName());
        response.setClassDates(classDates);

        return response;
    }

    @Transactional(readOnly = true)
    public StudentAttendanceRecordDTO getStudentAttendanceRecord(Long studentId, Long groupId, Long periodId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        GroupClass group = groupClassRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        // Verify student is enrolled in this group
        if (!student.getEnrolledGroups().contains(group)) {
            throw new CustomException(ErrorCode.STUDENT_NOT_IN_GROUP);
        }

        if (period.getStartDate() == null || period.getEndDate() == null) {
            throw new CustomException(ErrorCode.PERIOD_DATES_MISSING);
        }

        // Get all class dates for this group and period
        List<LocalDate> classDates = calculateClassDates(group, period.getStartDate(), period.getEndDate());

        if (classDates.isEmpty()) {
            throw new CustomException(ErrorCode.NO_CLASSES_IN_PERIOD);
        }

        // Get student's attendance records
        List<Attendance> attendanceRecords = attendanceRepository
                .findByStudentIdAndGroupIdAndPeriodId(studentId, groupId, periodId);

        // Map class dates to attendance status
        Map<LocalDate, Boolean> attendanceMap = new LinkedHashMap<>();
        for (LocalDate date : classDates) {
            if (date.isAfter(LocalDate.now())) {
                continue; // Skip future dates
            }

            // Find attendance record for this date
            Optional<Attendance> record = attendanceRecords.stream()
                    .filter(a -> a.getClassDate().equals(date))
                    .findFirst();

            // If record exists, use its value; otherwise mark as absent
            attendanceMap.put(date, record.map(Attendance::getPresent).orElse(false));
        }

        // Calculate attendance statistics
        int totalClasses = attendanceMap.size();
        int presentCount = (int) attendanceMap.values().stream().filter(present -> present).count();
        double attendancePercentage = totalClasses > 0 ? (double) presentCount / totalClasses * 100 : 0;

        // Create response DTO
        StudentAttendanceRecordDTO response = new StudentAttendanceRecordDTO();
        response.setStudentId(student.getId());
        response.setStudentName(student.getName());
        response.setGroupId(group.getId());
        response.setGroupName(group.getName());
        response.setSubjectName(group.getSubject().getName());
        response.setAttendanceRecord(attendanceMap);
        response.setTotalClasses(totalClasses);
        response.setPresentCount(presentCount);
        response.setAttendancePercentage(attendancePercentage);

        return response;
    }

    @Transactional(readOnly = true)
    public List<StudentAttendanceRecordDTO> getGroupAttendanceRecords(Long groupId, Long periodId) {
        GroupClass group = groupClassRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        if (period.getStartDate() == null || period.getEndDate() == null) {
            throw new CustomException(ErrorCode.PERIOD_DATES_MISSING);
        }

        List<StudentAttendanceRecordDTO> records = new ArrayList<>();

        for (Student student : group.getStudents()) {
            try {
                StudentAttendanceRecordDTO record = getStudentAttendanceRecord(student.getId(), groupId, periodId);
                records.add(record);
            } catch (CustomException e) {
                // Skip students with errors but don't fail the whole operation
                continue;
            }
        }

        return records;
    }

    // Helper method to check if a date corresponds to a scheduled class day
    private boolean isScheduledClassDay(GroupClass group, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        return group.getSchedules().stream()
                .anyMatch(schedule -> {
                    String scheduleDayName = schedule.getDay().toUpperCase();
                    DayOfWeek scheduleDay = mapDayNameToDayOfWeek(scheduleDayName);
                    return scheduleDay == dayOfWeek;
                });
    }

    // Helper method to map day names to DayOfWeek
    private DayOfWeek mapDayNameToDayOfWeek(String dayName) {
        switch (dayName) {
            case "LUNES": return DayOfWeek.MONDAY;
            case "MARTES": return DayOfWeek.TUESDAY;
            case "MIERCOLES": return DayOfWeek.WEDNESDAY;
            case "JUEVES": return DayOfWeek.THURSDAY;
            case "VIERNES": return DayOfWeek.FRIDAY;
            case "SABADO": return DayOfWeek.SATURDAY;
            case "DOMINGO": return DayOfWeek.SUNDAY;
            default: return null;
        }
    }

    // Helper method to calculate all class dates for a group within a period
    private List<LocalDate> calculateClassDates(GroupClass group, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> classDates = new ArrayList<>();

        // Get all schedule days for this group
        Map<DayOfWeek, Schedule> scheduleDays = new HashMap<>();
        for (Schedule schedule : group.getSchedules()) {
            DayOfWeek day = mapDayNameToDayOfWeek(schedule.getDay().toUpperCase());
            if (day != null) {
                scheduleDays.put(day, schedule);
            }
        }

        if (scheduleDays.isEmpty()) {
            return classDates; // No scheduled days
        }

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            DayOfWeek currentDay = current.getDayOfWeek();
            if (scheduleDays.containsKey(currentDay)) {
                classDates.add(current);
            }
            current = current.plusDays(1);
        }

        return classDates;
    }
}