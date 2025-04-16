package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.academicRecord.AcademicRecordDTO;
import co.edu.udes.backend.dto.enrollment.CareerEnrollmentDTO;
import co.edu.udes.backend.dto.enrollment.EnrollmentDTO;
import co.edu.udes.backend.dto.schedule.ClassScheduleDTO;
import co.edu.udes.backend.dto.schedule.ScheduleStudentDTO;
import co.edu.udes.backend.dto.student.*;
import co.edu.udes.backend.dto.subject.SubjectStatusDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.student.StudentMapper;
import co.edu.udes.backend.models.*;
import co.edu.udes.backend.repositories.CareerRepository;
import co.edu.udes.backend.repositories.GroupClassRepository;
import co.edu.udes.backend.repositories.StudentRepository;
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

    public StudentService(StudentRepository studentRepository, CareerRepository careerRepository, GroupClassRepository groupClassRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.careerRepository = careerRepository;
        this.groupClassRepository = groupClassRepository;
        this.studentMapper = studentMapper;
    }

    public StudentResponseDTO create(StudentDTO studentDTO) {
        if (studentRepository.existsByCode(studentDTO.getCode())) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new CustomException(ErrorCode.STUDENT_ALREADY_EXISTS);
        }

        Student student = studentMapper.toEntity(studentDTO);

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

        ScheduleStudentDTO scheduleDTO = new ScheduleStudentDTO();
        scheduleDTO.setStudentId(student.getId());
        scheduleDTO.setStudentName(student.getName());

        List<ClassScheduleDTO> schedules = new ArrayList<>();

        for (GroupClass group : student.getEnrolledGroups()) {
            for (Schedule schedule : group.getSchedules()) {
                ClassScheduleDTO classSchedule = new ClassScheduleDTO(
                        group.getId(),
                        group.getSubject().getName(),
                        schedule.getDay(),
                        schedule.getStartTime(),
                        schedule.getEndTime(),
                        schedule.getClassroom()
                );
                schedules.add(classSchedule);
            }
        }

        scheduleDTO.setSchedules(schedules);
        return scheduleDTO;
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
}