package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.grade.AssignGradeDTO;
import co.edu.udes.backend.dto.period.PeriodCreateDTO;
import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import co.edu.udes.backend.dto.period.PeriodUpdateDTO;
import co.edu.udes.backend.dto.subject.PeriodGradeDTO;
import co.edu.udes.backend.dto.subject.SubjectFinalGradeDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.period.PeriodMapper;
import co.edu.udes.backend.models.Grade;
import co.edu.udes.backend.models.Period;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Subject;
import co.edu.udes.backend.repositories.GradeRepository;
import co.edu.udes.backend.repositories.PeriodRepository;
import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.repositories.SubjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PeriodService {
    private final PeriodRepository periodRepository;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final PeriodMapper periodMapper;

    public PeriodService(PeriodRepository periodRepository,
                         StudentRepository studentRepository,
                         GradeRepository gradeRepository,
                         SubjectRepository subjectRepository,
                         PeriodMapper periodMapper) {
        this.periodRepository = periodRepository;
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.subjectRepository = subjectRepository;
        this.periodMapper = periodMapper;
    }

    public PeriodResponseDTO create(PeriodCreateDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        // Check if period with same name already exists for this student
        if (periodRepository.existsByNameAndStudentId(dto.getName(), dto.getStudentId())) {
            throw new CustomException(ErrorCode.PERIOD_ALREADY_EXISTS);
        }

        Period period = periodMapper.toEntity(dto, student);
        Period savedPeriod = periodRepository.save(period);
        return periodMapper.toResponseDTO(savedPeriod);
    }

    public List<PeriodResponseDTO> getAllByStudentId(Long studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new CustomException(ErrorCode.STUDENT_NOT_FOUND);
        }

        return periodRepository.findByStudentIdOrderByStartDateAsc(studentId).stream()
                .map(periodMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public PeriodResponseDTO getById(Long id) {
        Period period = periodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));
        return periodMapper.toResponseDTO(period);
    }

    public PeriodResponseDTO update(Long id, PeriodUpdateDTO dto) {
        Period existingPeriod = periodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        periodMapper.updatePeriodFromDTO(dto, existingPeriod);
        Period updatedPeriod = periodRepository.save(existingPeriod);
        return periodMapper.toResponseDTO(updatedPeriod);
    }

    public void delete(Long id) {
        Period period = periodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        periodRepository.delete(period);
    }

    @Transactional
    public void calculateFinalGrades(Long periodId) {
        Period period = periodRepository.findById(periodId)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        // Group grades by subject
        Map<Long, List<Grade>> gradesBySubject = period.getGrades().stream()
                .collect(Collectors.groupingBy(grade -> grade.getSubject().getId()));

        // Calculate average grade for each subject
        double sumGrades = 0.0;
        int subjectCount = gradesBySubject.size();

        for (List<Grade> subjectGrades : gradesBySubject.values()) {
            double avgGrade = subjectGrades.stream()
                    .mapToDouble(Grade::getValue)
                    .average()
                    .orElse(0.0);

            sumGrades += avgGrade;
        }

        // Calculate final grade as average of subject grades
        double finalGrade = subjectCount > 0 ? sumGrades / subjectCount : 0.0;

        // Update period with final grade and approval status
        period.setFinalGrade(finalGrade);
        period.setApproved(finalGrade >= 3.0); // Assuming passing grade is 3.0

        periodRepository.save(period);
    }

    @Transactional
    public void generatePeriodsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        // Define academic period dates (you may want to get these from configuration)
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.withDayOfMonth(1);
        LocalDate endDate = currentDate.plusMonths(4);

        // Delete existing periods if any
        periodRepository.deleteByStudentId(studentId);

        // Create 3 periods with equal weights
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        long periodDays = totalDays / 3; // 3 periods

        LocalDate periodStart = startDate;

        for (int i = 1; i <= 3; i++) {
            Period period = new Period();
            period.setName("Corte " + i);
            period.setStudent(student);
            period.setWeight(0.33); // Approximately 33% each
            period.setStartDate(periodStart);

            // Calculate end date for this period
            LocalDate periodEnd;
            if (i == 3) {
                // Last period ends with end date
                periodEnd = endDate;
            } else {
                periodEnd = periodStart.plusDays(periodDays - 1);
            }

            period.setEndDate(periodEnd);
            periodRepository.save(period);

            // Next period starts the day after
            periodStart = periodEnd.plusDays(1);
        }
    }

    @Transactional
    public PeriodResponseDTO assignGrade(AssignGradeDTO dto) {
        Period period = periodRepository.findById(dto.getPeriodId())
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));

        // Check if student is enrolled in this subject
        Student student = period.getStudent();
        boolean isEnrolled = student.getEnrolledGroups().stream()
                .anyMatch(group -> group.getSubject().getId().equals(subject.getId()));

        if (!isEnrolled) {
            throw new CustomException(ErrorCode.STUDENT_NOT_ENROLLED);
        }

        // Find existing grade or create new one
        Grade grade = period.getGrades().stream()
                .filter(g -> g.getSubject().getId().equals(subject.getId()))
                .findFirst()
                .orElse(new Grade());

        grade.setPeriod(period);
        grade.setSubject(subject);
        grade.setValue(dto.getValue());

        if (!period.getGrades().contains(grade)) {
            period.getGrades().add(grade);
        }

        gradeRepository.save(grade);
        Period updatedPeriod = periodRepository.save(period);

        return periodMapper.toResponseDTO(updatedPeriod);
    }

    @Transactional(readOnly = true)
    public List<SubjectFinalGradeDTO> calculateFinalSubjectGrades(Long studentId, Long subjectId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));

        // Verificar que el estudiante esté inscrito en la materia
        boolean isEnrolled = student.getEnrolledGroups().stream()
                .anyMatch(group -> group.getSubject().getId().equals(subjectId));

        if (!isEnrolled) {
            throw new CustomException(ErrorCode.STUDENT_NOT_ENROLLED);
        }

        // Obtener todos los periodos del estudiante que tengan calificaciones para la materia especificada
        List<Period> allPeriods = periodRepository.findByStudentIdOrderByStartDateAsc(studentId);

        // Filtrar periodos que tienen calificaciones para la materia
        List<Period> periodsWithSubjectGrades = allPeriods.stream()
                .filter(period -> period.getGrades().stream()
                        .anyMatch(grade -> grade.getSubject().getId().equals(subjectId)))
                .collect(Collectors.toList());

        if (periodsWithSubjectGrades.isEmpty()) {
            throw new CustomException(ErrorCode.NO_GRADES_FOUND_FOR_SUBJECT);
        }

        // Verificar que haya 3 periodos con calificaciones para la materia
        if (periodsWithSubjectGrades.size() != 3) {
            throw new CustomException(ErrorCode.INCOMPLETE_PERIODS_FOR_SUBJECT);
        }

        // Asignar los pesos correctos a cada periodo (0.3, 0.3, 0.4)
        periodsWithSubjectGrades.get(0).setWeight(0.3);
        periodsWithSubjectGrades.get(1).setWeight(0.3);
        periodsWithSubjectGrades.get(2).setWeight(0.4);

        // Guardar los cambios de peso
        periodRepository.saveAll(periodsWithSubjectGrades);

        List<SubjectFinalGradeDTO> results = new ArrayList<>();

        // Calcular la nota final para la materia especificada
        SubjectFinalGradeDTO finalGrade = calculateFinalGradeForSubject(subject, periodsWithSubjectGrades);
        results.add(finalGrade);

        return results;
    }

    @Transactional(readOnly = true)
    public List<SubjectFinalGradeDTO> calculateAllFinalSubjectGrades(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        // Obtener todas las materias en las que está inscrito el estudiante
        List<Subject> enrolledSubjects = student.getEnrolledGroups().stream()
                .map(group -> group.getSubject())
                .distinct()
                .collect(Collectors.toList());

        List<SubjectFinalGradeDTO> results = new ArrayList<>();

        // Para cada materia, calcular la nota final
        for (Subject subject : enrolledSubjects) {
            try {
                // Obtener periodos con calificaciones para esta materia
                List<Period> allPeriods = periodRepository.findByStudentIdOrderByStartDateAsc(studentId);
                List<Period> periodsWithSubjectGrades = allPeriods.stream()
                        .filter(period -> period.getGrades().stream()
                                .anyMatch(grade -> grade.getSubject().getId().equals(subject.getId())))
                        .collect(Collectors.toList());

                // Si hay exactamente 3 periodos con notas para esta materia, calcular la nota final
                if (periodsWithSubjectGrades.size() == 3) {
                    // Asignar los pesos correctos (0.3, 0.3, 0.4)
                    periodsWithSubjectGrades.get(0).setWeight(0.3);
                    periodsWithSubjectGrades.get(1).setWeight(0.3);
                    periodsWithSubjectGrades.get(2).setWeight(0.4);

                    // Guardar los cambios de peso
                    periodRepository.saveAll(periodsWithSubjectGrades);

                    // Calcular la nota final
                    SubjectFinalGradeDTO finalGrade = calculateFinalGradeForSubject(subject, periodsWithSubjectGrades);
                    results.add(finalGrade);
                }
                // Si no hay exactamente 3 periodos, omitir esta materia del cálculo
            } catch (Exception e) {
                // Si hay algún error, continuar con la siguiente materia
                continue;
            }
        }

        return results;
    }

    private SubjectFinalGradeDTO calculateFinalGradeForSubject(Subject subject, List<Period> periods) {
        List<PeriodGradeDTO> periodGrades = new ArrayList<>();
        double weightedSum = 0.0;
        double totalWeight = 0.0;

        for (Period period : periods) {
            // Buscar la nota de esta materia en este periodo
            double gradeValue = period.getGrades().stream()
                    .filter(grade -> grade.getSubject().getId().equals(subject.getId()))
                    .map(Grade::getValue)
                    .findFirst()
                    .orElse(0.0); // Si no hay nota, se considera 0

            // Agregar esta nota al cálculo ponderado
            weightedSum += gradeValue * period.getWeight();
            totalWeight += period.getWeight();

            // Guardar la información de esta nota por periodo
            periodGrades.add(PeriodGradeDTO.builder()
                    .periodId(period.getId())
                    .periodName(period.getName())
                    .grade(gradeValue)
                    .weight(period.getWeight())
                    .build());
        }

        // Calcular la nota final ponderada
        double finalGrade = totalWeight > 0 ? weightedSum / totalWeight : 0.0;
        boolean approved = finalGrade >= 3.0; // Aprobado con 3.0 o más

        return SubjectFinalGradeDTO.builder()
                .subjectId(subject.getId())
                .subjectName(subject.getName())
                .periodGrades(periodGrades)
                .finalGrade(finalGrade)
                .approved(approved)
                .build();
    }
}