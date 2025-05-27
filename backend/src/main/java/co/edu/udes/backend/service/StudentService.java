package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.academicRecord.AcademicRecordDTO;
import co.edu.udes.backend.dto.enrollment.CareerEnrollmentDTO;
import co.edu.udes.backend.dto.enrollment.EnrollmentDTO;
import co.edu.udes.backend.dto.loan.LoanResponseDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.dto.period.InitializePeriodsDTO;
import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import co.edu.udes.backend.dto.period.PeriodWithSubjectsDTO;
import co.edu.udes.backend.dto.schedule.ClassScheduleDTO;
import co.edu.udes.backend.dto.schedule.DayScheduleDTO;
import co.edu.udes.backend.dto.schedule.ScheduleStudentDTO;
import co.edu.udes.backend.dto.student.*;
import co.edu.udes.backend.dto.subject.SubjectGradeDTO;
import co.edu.udes.backend.dto.subject.SubjectStatusDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.loan.LoanMapper;
import co.edu.udes.backend.mappers.reserve.ReserveMapper;
import co.edu.udes.backend.mappers.period.PeriodMapper;
import co.edu.udes.backend.mappers.student.StudentMapper;
import co.edu.udes.backend.models.*;
import co.edu.udes.backend.repositories.CareerRepository;
import co.edu.udes.backend.repositories.GroupClassRepository;
import co.edu.udes.backend.repositories.ReserveRepository;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final CareerRepository careerRepository;
    private final GroupClassRepository groupClassRepository;
    private final StudentMapper studentMapper;
    private final PeriodMapper periodMapper;
    private final SemesterRepository semesterRepository;
    private final PeriodRepository periodRepository;
    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;
    @Autowired
    private RoleService roleService;
    private final ReserveRepository reserveRepository;
    private final ReserveMapper reserveMapper;

    @Autowired
    public StudentService(
            StudentRepository studentRepository,
            CareerRepository careerRepository,
            GroupClassRepository groupClassRepository,
            StudentMapper studentMapper,
            ReserveRepository reserveRepository,
            ReserveMapper reserveMapper,
            PeriodMapper periodMapper,
            SemesterRepository semesterRepository,
            PeriodRepository periodRepository,
            LoanRepository loanRepository,
            LoanMapper loanMapper
    ) {
        this.studentRepository = studentRepository;
        this.careerRepository = careerRepository;
        this.groupClassRepository = groupClassRepository;
        this.studentMapper = studentMapper;
        this.reserveRepository = reserveRepository;
        this.reserveMapper = reserveMapper;
        this.periodMapper = periodMapper;
        this.semesterRepository = semesterRepository;
        this.periodRepository = periodRepository;
        this.loanRepository=loanRepository;
        this.loanMapper=loanMapper;

    }

    public StudentResponseDTO create(StudentDTO studentDTO) {
        if (studentRepository.existsByCode(studentDTO.getCode())) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        Student student = studentMapper.toEntity(studentDTO);


        student.setRole(roleService.getRoleByName(RoleService.ROLE_STUDENT));

        Student savedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(savedStudent);
    }

    public List<StudentResponseDTO> findAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));
        return studentMapper.toResponseDTO(student);
    }

    public StudentResponseDTO update(Long id, StudentDTO studentDTO) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        if (!existing.getCode().equals(studentDTO.getCode()) &&
                studentRepository.existsByCode(studentDTO.getCode())) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        if (!existing.getEmail().equals(studentDTO.getEmail()) &&
                studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        Student studentResponse = studentMapper.toEntity(studentDTO);

        Student updatedStudent = studentRepository.save(studentResponse);
        return studentMapper.toResponseDTO(updatedStudent);
    }

    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new CustomException(ErrorCode.STUDENT_NOT_FOUND);
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public StudentResponseDTO enrollInCareer(CareerEnrollmentDTO enrollmentDTO) {
        Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        Career career = careerRepository.findById(enrollmentDTO.getCareerId())
                .orElseThrow(() -> new CustomException(ErrorCode.CAREER_NOT_FOUND));

        student.setCareer(career);
        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(updatedStudent);
    }

    @Transactional
    public StudentResponseDTO enrollInGroup(EnrollmentDTO enrollmentDTO) {
        Student student = studentRepository.findById(enrollmentDTO.getStudentId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        GroupClass group = groupClassRepository.findById(enrollmentDTO.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        if (student.getCareer() == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_ENROLLED_IN_CAREER);
        }

        Subject subject = group.getSubject();


        boolean subjectInCareer = student.getCareer().getSemesters().stream()
                .flatMap(semester -> semester.getSubjects().stream())
                .anyMatch(s -> s.getId().equals(subject.getId()));

        System.out.println("Career semesters: " + student.getCareer().getSemesters().size());
        student.getCareer().getSemesters().forEach(semester -> {
            System.out.println("Semester " + semester.getNumber() + " subjects: " +
                    semester.getSubjects().stream().map(Subject::getName).collect(Collectors.joining(", ")));
        });

        if (!subjectInCareer) {
            throw new CustomException(ErrorCode.SUBJECT_NOT_IN_CAREER);
        }

        if (subject.getPrerequisites() != null && !subject.getPrerequisites().isEmpty()) {
            Set<Subject> passedSubjects = student.getEnrolledGroups().stream()
                    .map(GroupClass::getSubject)
                    .collect(Collectors.toSet());


            for (Subject prerequisite : subject.getPrerequisites()) {
                if (!passedSubjects.contains(prerequisite)) {
                    throw new CustomException(ErrorCode.PREREQUISITE_NOT_COMPLETED);
                }
            }
        }


        if (group.getEnrolledCount() >= group.getCapacity()) {
            throw new CustomException(ErrorCode.GROUP_FULL);
        }


        if (student.getEnrolledGroups().contains(group)) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_ENROLLED);
        }

        student.getEnrolledGroups().add(group);
        group.getStudents().add(student);

        group.setEnrolledCount(group.getEnrolledCount() + 1);

        groupClassRepository.save(group);
        Student updatedStudent = studentRepository.save(student);

        return studentMapper.toResponseDTO(updatedStudent);
    }

    @Transactional(readOnly = true)
    public AcademicRecordDTO getAcademicRecord(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        if (student.getCareer() == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_ENROLLED_IN_CAREER);
        }

        AcademicRecordDTO academicRecord = new AcademicRecordDTO();
        academicRecord.setStudentId(student.getId());
        academicRecord.setStudentName(student.getName());
        academicRecord.setCareerName(student.getCareer().getName());

        List<Subject> allCareerSubjects = student.getCareer().getSemesters().stream()
                .flatMap(semester -> semester.getSubjects().stream())
                .collect(Collectors.toList());

        Set<Subject> enrolledSubjects = student.getEnrolledGroups().stream()
                .map(GroupClass::getSubject)
                .collect(Collectors.toSet());

        List<SubjectStatusDTO> inProgressSubjects = enrolledSubjects.stream()
                .map(subject -> {
                    Optional<Semester> semesterOpt = student.getCareer().getSemesters().stream()
                            .filter(s -> s.getSubjects().contains(subject))
                            .findFirst();

                    int semester = semesterOpt.isPresent() ? semesterOpt.get().getNumber() : 0;

                    return new SubjectStatusDTO(
                            subject.getId(),
                            subject.getName(),
                            semester,
                            "IN_PROGRESS"
                    );
                })
                .collect(Collectors.toList());

        academicRecord.setInProgressSubjects(inProgressSubjects);
        academicRecord.setCompletedSubjects(new ArrayList<>());

        List<SubjectStatusDTO> pendingSubjects = allCareerSubjects.stream()
                .filter(subject -> !enrolledSubjects.contains(subject))
                .map(subject -> {
                    Optional<Semester> semesterOpt = student.getCareer().getSemesters().stream()
                            .filter(s -> s.getSubjects().contains(subject))
                            .findFirst();

                    int semester = semesterOpt.isPresent() ? semesterOpt.get().getNumber() : 0;

                    return new SubjectStatusDTO(
                            subject.getId(),
                            subject.getName(),

                            semester,
                            "PENDING"
                    );
                })
                .collect(Collectors.toList());

        academicRecord.setPendingSubjects(pendingSubjects);


        int currentSemester = inProgressSubjects.stream()
                .mapToInt(SubjectStatusDTO::getSemester)
                .max()
                .orElse(1);

        academicRecord.setCurrentSemester(currentSemester);

        return academicRecord;
    }

    @Transactional(readOnly = true)
    public ScheduleStudentDTO getSchedule(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        if (student.getEnrolledGroups() == null || student.getEnrolledGroups().isEmpty()) {
            throw new CustomException(ErrorCode.NO_ENROLLED_GROUPS);
        }

        // Crear el DTO principal
        ScheduleStudentDTO scheduleDTO = new ScheduleStudentDTO();
        scheduleDTO.setStudentId(student.getId());
        scheduleDTO.setStudentName(student.getName());

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

        // Recorrer los grupos y horarios del estudiante
        for (GroupClass group : student.getEnrolledGroups()) {
            for (Schedule schedule : group.getSchedules()) {
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

    @Transactional
    public StudentResponseDTO cancelGroupEnrollment(Long studentId, Long groupId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        GroupClass group = groupClassRepository.findById(groupId)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));


        if (!student.getEnrolledGroups().contains(group)) {
            throw new CustomException(ErrorCode.STUDENT_NOT_ENROLLED);
        }


        student.getEnrolledGroups().remove(group);

        group.getStudents().remove(student);

        group.setEnrolledCount(group.getEnrolledCount() - 1);

        groupClassRepository.save(group);
        Student updatedStudent = studentRepository.save(student);

        return StudentResponseDTO.fromEntity(updatedStudent);
    }

    public List<ReserveResponseDTO> getReservesByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        List<Reserve> reserves = reserveRepository.findByStudent(student);
        return reserves.stream()
                .map(reserveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    @Transactional
    public void initializeStudentPeriods(InitializePeriodsDTO initializeDTO) {
        Student student = studentRepository.findById(initializeDTO.getStudentId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        if (student.getCareer() == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_ENROLLED_IN_CAREER);
        }

        // Validar la configuración de períodos
        if (initializeDTO.getPeriodConfigurations() == null || initializeDTO.getPeriodConfigurations().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_PERIOD_CONFIGURATION);
        }

        // Verificar que los pesos sumen 1.0 (100%)
        double totalWeight = initializeDTO.getPeriodConfigurations().stream()
                .mapToDouble(InitializePeriodsDTO.PeriodDateRangeDTO::getWeight)
                .sum();

        if (Math.abs(totalWeight - 1.0) > 0.01) {
            throw new CustomException(ErrorCode.INVALID_PERIOD_WEIGHTS);
        }

        // Verificar que las fechas sean válidas
        for (InitializePeriodsDTO.PeriodDateRangeDTO periodConfig : initializeDTO.getPeriodConfigurations()) {
            if (periodConfig.getStartDate() == null || periodConfig.getEndDate() == null) {
                throw new CustomException(ErrorCode.MISSING_PERIOD_DATES);
            }

            if (periodConfig.getStartDate().isAfter(periodConfig.getEndDate())) {
                throw new CustomException(ErrorCode.INVALID_PERIOD_DATES);
            }
        }

        // Eliminar períodos existentes
        student.getPeriods().clear();

        // Crear nuevos períodos para cada semestre
        for (Semester semester : student.getCareer().getSemesters()) {
            for (InitializePeriodsDTO.PeriodDateRangeDTO periodConfig : initializeDTO.getPeriodConfigurations()) {
                Period period = new Period();
                period.setName(periodConfig.getName());
                period.setWeight(periodConfig.getWeight());
                period.setStartDate(periodConfig.getStartDate());
                period.setEndDate(periodConfig.getEndDate());
                period.setStudent(student);
                period.setSemester(semester);
                student.getPeriods().add(period);
            }
        }

        studentRepository.save(student);
    }

    @Transactional(readOnly = true)
    public List<StudentSemesterPeriodsDTO> getStudentPeriods(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        if (student.getCareer() == null) {
            throw new CustomException(ErrorCode.STUDENT_NOT_ENROLLED_IN_CAREER);
        }

        List<StudentSemesterPeriodsDTO> result = new ArrayList<>();

        // Group periods by semester
        Map<Semester, List<Period>> periodsBySemester = student.getPeriods().stream()
                .collect(Collectors.groupingBy(Period::getSemester));

        // For each semester, create a DTO with its periods
        for (Map.Entry<Semester, List<Period>> entry : periodsBySemester.entrySet()) {
            Semester semester = entry.getKey();
            List<Period> periods = entry.getValue();

            StudentSemesterPeriodsDTO semesterDTO = new StudentSemesterPeriodsDTO();
            semesterDTO.setSemesterId(semester.getId());
            semesterDTO.setSemesterNumber(semester.getNumber());

            List<PeriodWithSubjectsDTO> periodDTOs = new ArrayList<>();
            for (Period period : periods) {
                co.edu.udes.backend.dto.period.PeriodWithSubjectsDTO periodDTO = new co.edu.udes.backend.dto.period.PeriodWithSubjectsDTO();
                periodDTO.setPeriodId(period.getId());
                periodDTO.setNamePeriod(period.getName());
                periodDTO.setWeight(period.getWeight());

                // Get all enrolled subjects for this semester
                Map<Subject, Double> subjectsWithGrades = new HashMap<>();

                // Find all subjects for this semester that the student is enrolled in
                Set<Subject> enrolledSubjects = student.getEnrolledGroups().stream()
                        .map(GroupClass::getSubject)
                        .filter(subject -> subject.getSemester().equals(semester))
                        .collect(Collectors.toSet());

                // For each subject, find the grade for this period if exists
                Map<String, co.edu.udes.backend.dto.period.SubjectGradeDTO> subjectGrades = new HashMap<>();

                for (Subject subject : enrolledSubjects) {
                    co.edu.udes.backend.dto.period.SubjectGradeDTO gradeInfo = new co.edu.udes.backend.dto.period.SubjectGradeDTO();
                    gradeInfo.setId(subject.getId());

                    // Find grade for this subject in this period
                    Optional<Grade> grade = period.getGrades().stream()
                            .filter(g -> g.getSubject().getId().equals(subject.getId()))
                            .findFirst();

                    gradeInfo.setNoteStudent(grade.map(Grade::getValue).orElse(null));
                    subjectGrades.put(subject.getName(), gradeInfo);
                }

                periodDTO.setSubjects(subjectGrades);
                periodDTOs.add(periodDTO);
            }

            semesterDTO.setPeriods(periodDTOs);
            result.add(semesterDTO);
        }

        return result;
    }
    public List<LoanResponseDTO> getLoansByStudent(Long teacherId) {
        Student student = studentRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        List<Loan> Loans = loanRepository.findByStudent(student);
        return Loans.stream()
                .map(loanMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


}