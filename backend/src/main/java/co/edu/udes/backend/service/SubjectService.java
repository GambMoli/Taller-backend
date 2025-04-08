package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.subject.SubjectResponseDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.Career;
import co.edu.udes.backend.models.Semester;
import co.edu.udes.backend.models.Subject;
import co.edu.udes.backend.repositories.CareerRepository;
import co.edu.udes.backend.repositories.SemesterRepository;
import co.edu.udes.backend.repositories.SubjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final CareerRepository careerRepository;
    private final SemesterRepository semesterRepository;

    public SubjectService(SubjectRepository subjectRepository,
                          CareerRepository careerRepository,
                          SemesterRepository semesterRepository) {
        this.subjectRepository = subjectRepository;
        this.careerRepository = careerRepository;
        this.semesterRepository = semesterRepository;
    }

    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(SubjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new CustomException(ErrorCode.SUBJECT_NOT_FOUND);
        }
        subjectRepository.deleteById(id);
    }

    public List<SubjectResponseDTO> getAllSubjectsOptimized() {
        return subjectRepository.findAllWithCareersAndPrerequisite().stream()
                .map(SubjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public SubjectResponseDTO findById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));
        return SubjectResponseDTO.fromEntity(subject);
    }

    public Subject create(Subject subject, Long semesterId) {
        // Verificar si ya existe una materia con el mismo nombre
        if (subjectRepository.existsByName(subject.getName())) {
            throw new CustomException(ErrorCode.SUBJECT_ALREADY_EXISTS);
        }

        // Asignar semestre si se proporciona
        if (semesterId != null) {
            Semester semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));
            subject.setSemester(semester);

            // Validar prerrequisitos basados en el semestre
            validatePrerequisites(subject, semester);
        }

        // Verificar si los prerrequisitos existen
        if (subject.getPrerequisites() != null && !subject.getPrerequisites().isEmpty()) {
            subject.getPrerequisites().forEach(prerequisite -> {
                if (!subjectRepository.existsById(prerequisite.getId())) {
                    throw new CustomException(ErrorCode.PREREQUISITE_NOT_FOUND);
                }
            });
        }

        return subjectRepository.save(subject);
    }

    private void validatePrerequisites(Subject subject, Semester currentSemester) {
        if (subject.getPrerequisites() == null || subject.getPrerequisites().isEmpty()) {
            return;
        }

        for (Subject prerequisite : subject.getPrerequisites()) {
            Subject actualPrereq = subjectRepository.findById(prerequisite.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PREREQUISITE_NOT_FOUND));

            if (actualPrereq.getSemester() == null) {
                continue; // Omitir validación si el prerrequisito no tiene semestre asignado
            }

            // Verificar que el prerrequisito esté en un semestre anterior
            if (actualPrereq.getSemester().getNumber() >= currentSemester.getNumber()) {
                throw new CustomException(ErrorCode.INVALID_PREREQUISITE_SEMESTER);
            }
        }
    }

    public List<SubjectResponseDTO> getSubjectsByCareer(Long careerId) {
        if (!careerRepository.existsById(careerId)) {
            throw new CustomException(ErrorCode.CAREER_NOT_FOUND);
        }

        List<Subject> subjects = subjectRepository.findByCareers_Id(careerId);
        return subjects.stream()
                .map(SubjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


    public SubjectResponseDTO update(Long id, Subject subjectDetails, Long semesterId) {
        Subject existingSubject = subjectRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));

        // Verificar si el nuevo nombre ya existe en otra materia
        if (!existingSubject.getName().equals(subjectDetails.getName()) &&
                subjectRepository.existsByName(subjectDetails.getName())) {
            throw new CustomException(ErrorCode.SUBJECT_ALREADY_EXISTS);
        }

        // Actualizar semestre si se proporciona
        if (semesterId != null) {
            Semester semester = semesterRepository.findById(semesterId)
                    .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));
            existingSubject.setSemester(semester);

            // Validar prerrequisitos con el nuevo semestre
            if (subjectDetails.getPrerequisites() != null) {
                validatePrerequisites(subjectDetails, semester);
            }
        }

        // Verificar si los prerrequisitos existen
        if (subjectDetails.getPrerequisites() != null && !subjectDetails.getPrerequisites().isEmpty()) {
            subjectDetails.getPrerequisites().forEach(prerequisite -> {
                if (!subjectRepository.existsById(prerequisite.getId())) {
                    throw new CustomException(ErrorCode.PREREQUISITE_NOT_FOUND);
                }
            });
        }

        // Actualiza los campos de la entidad
        existingSubject.setName(subjectDetails.getName());
        existingSubject.setPrerequisites(subjectDetails.getPrerequisites());

        // Guarda la entidad actualizada
        Subject updatedSubject = subjectRepository.save(existingSubject);

        // Convierte la entidad actualizada a DTO
        return SubjectResponseDTO.fromEntity(updatedSubject);
    }



    public void assignToSemester(Long subjectId, Long semesterId) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));

        Semester newSemester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));

        // Verificar si ya está asignada a un semestre de la misma carrera
        Semester currentSemester = subject.getSemester();

        if (currentSemester != null) {
            // Si el semestre actual pertenece a la misma carrera que el nuevo semestre, no permite esto:
            if (currentSemester.getCareer().getId().equals(newSemester.getCareer().getId())) {
                // La materia ya está asignada a un semestre de esta carrera, se puede actualizar
                // O lanzar excepción si no quieres permitir //Gabo
                throw new CustomException(ErrorCode.SUBJECT_ALREADY_ASSIGNED,
                        "La materia ya está asignada al semestre " + currentSemester.getNumber() +
                                " de la misma carrera. Use el endpoint de actualización para cambiar el semestre.");
            }

            Subject newSubject = new Subject();
            newSubject.setName(subject.getName());
            newSubject.setSemester(newSemester);

            subjectRepository.save(newSubject);
            return;
        }

        // Validar que las materias prerrequisito estén asignadas a semestres anteriores
        if (subject.getPrerequisites() != null && !subject.getPrerequisites().isEmpty()) {
            for (Subject prereq : subject.getPrerequisites()) {
                Subject fullPrereq = subjectRepository.findByIdWithSemester(prereq.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.PREREQUISITE_NOT_FOUND));

                Semester prereqSemester = fullPrereq.getSemester();

                if (prereqSemester == null) {
                    throw new CustomException(ErrorCode.INVALID_PREREQUISITE_SEMESTER,
                            "La materia prerrequisito '" + fullPrereq.getName() + "' no ha sido asignada a ningún semestre.");
                }

                if (!prereqSemester.getCareer().getId().equals(newSemester.getCareer().getId())) {
                    throw new CustomException(ErrorCode.INVALID_PREREQUISITE_SEMESTER,
                            "La materia prerrequisito '" + fullPrereq.getName() + "' pertenece a otra carrera.");
                }

                if (prereqSemester.getNumber() >= newSemester.getNumber()) {
                    throw new CustomException(ErrorCode.INVALID_PREREQUISITE_SEMESTER,
                            "La materia prerrequisito '" + fullPrereq.getName() + "' debe estar en un semestre anterior.");
                }
            }
        }
        subject.setSemester(newSemester);
        subjectRepository.save(subject);
    }


    // Obtener materias por semestre
    public List<SubjectResponseDTO> getSubjectsBySemester(Long semesterId) {
        if (!semesterRepository.existsById(semesterId)) {
            throw new CustomException(ErrorCode.SEMESTER_NOT_FOUND);
        }

        return subjectRepository.findBySemesterId(semesterId).stream()
                .map(SubjectResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
