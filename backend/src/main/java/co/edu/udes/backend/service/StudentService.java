package co.edu.udes.backend.service;

import co.edu.udes.backend.enums.AcademicStatus;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.models.*;
import co.edu.udes.backend.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final GroupClassRepository groupRepository;
    private final AcademicRecordRepository academicRecordRepository;

    public StudentService(StudentRepository studentRepository,
                          GroupClassRepository groupRepository,
                          AcademicRecordRepository academicRecordRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.academicRecordRepository = academicRecordRepository;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student updated) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        student.setName(updated.getName());
        student.setEmail(updated.getEmail());
        student.setDocumentNumber(updated.getDocumentNumber());

        return studentRepository.save(student);
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
    }

    public List<AcademicRecord> getCurrentSubjects(Long studentId) {
        Student student = findById(studentId);
        return academicRecordRepository.findByStudent(student).stream()
                .filter(r -> r.getStatus() == AcademicStatus.EN_CURSO)
                .collect(Collectors.toList());
    }

    public List<AcademicRecord> getAllRecords(Long studentId) {
        Student student = findById(studentId);
        return academicRecordRepository.findByStudent(student);
    }

//    public List<AcademicRecord> enrollSubjects(Long studentId, List<Long> groupIds) {
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException(ErrorCode.STUDENT_NOT_FOUND.getMessage()));
//
//        // Verificar si la carrera del estudiante permite las materias
//        Career career = student.getCareer();
//        if (career == null) {
//            throw new RuntimeException(ErrorCode.CAREER_NOT_FOUND.getMessage());
//        }
//
//        List<AcademicRecord> enrolledRecords = new ArrayList<>();
//
//        for (Long groupId : groupIds) {
//            GroupClass group = groupRepository.findById(groupId)
//                    .orElseThrow(() -> new RuntimeException(ErrorCode.GROUP_NOT_FOUND.getMessage()));
//
//            Subject subject = group.getSubject();
//
//
//            if (!career.getSubjects().contains(subject)) {
//                throw new RuntimeException(ErrorCode.SUBJECT_NOT_FOUND.getMessage());
//            }
//
//            // Verificar si ya está inscrito
//            if (academicRecordRepository.existsByStudentAndSubject(student, subject)) {
//                throw new RuntimeException(ErrorCode.ALREADY_ENROLLED.getMessage());
//            }
//
//            // Verificar prerrequisito
//            Subject prereq = subject.getPrerequisite();
//            if (prereq != null) {
//                AcademicRecord prereqRecord = academicRecordRepository
//                        .findByStudentAndSubject(student, prereq)
//                        .orElseThrow(() -> new RuntimeException(ErrorCode.PREREQUISITE_NOT_FOUND.getMessage()));
//
//                if (prereqRecord.getStatus() != AcademicStatus.APROBADO) {
//                    throw new RuntimeException(ErrorCode.PREREQUISITE_NOT_APPROVED.getMessage());
//                }
//            }
//
//            // Crear nuevo registro académico
//            AcademicRecord record = new AcademicRecord();
//            record.setStudent(student);
//            record.setGroup(group);
//            record.setSubject(subject);
//            record.setStatus(AcademicStatus.EN_CURSO);
//
//            enrolledRecords.add(academicRecordRepository.save(record));
//        }
//
//        return enrolledRecords;
//    }

}
