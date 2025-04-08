package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.semester.SemesterResponseDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.Career;
import co.edu.udes.backend.models.Semester;
import co.edu.udes.backend.repositories.CareerRepository;
import co.edu.udes.backend.repositories.SemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SemesterService {
    private final SemesterRepository semesterRepository;
    private final CareerRepository careerRepository;

    public SemesterService(SemesterRepository semesterRepository, CareerRepository careerRepository) {
        this.semesterRepository = semesterRepository;
        this.careerRepository = careerRepository;
    }

    public Semester create(Semester semester, Long careerId) {
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new CustomException(ErrorCode.CAREER_NOT_FOUND));

        if (semesterRepository.existsByNumberAndCareerId(semester.getNumber(), careerId)) {
            throw new CustomException(ErrorCode.SEMESTER_ALREADY_EXISTS);
        }

        semester.setCareer(career);
        return semesterRepository.save(semester);
    }

    public List<SemesterResponseDTO> getAllByCareerId(Long careerId) {
        if (!careerRepository.existsById(careerId)) {
            throw new CustomException(ErrorCode.CAREER_NOT_FOUND);
        }

        return semesterRepository.findByCareerIdOrderByNumberAsc(careerId).stream()
                .map(SemesterResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public SemesterResponseDTO getById(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));
        return SemesterResponseDTO.fromEntity(semester);
    }

    public SemesterResponseDTO update(Long id, Semester semesterDetails) {
        Semester existingSemester = semesterRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));


        if (existingSemester.getNumber() != semesterDetails.getNumber() &&
                semesterRepository.existsByNumberAndCareerId(
                        semesterDetails.getNumber(), existingSemester.getCareer().getId())) {
            throw new CustomException(ErrorCode.SEMESTER_ALREADY_EXISTS);
        }

        existingSemester.setNumber(semesterDetails.getNumber());
        Semester updatedSemester = semesterRepository.save(existingSemester);

        return SemesterResponseDTO.fromEntity(updatedSemester);
    }

    public void delete(Long id) {
        Semester semester = semesterRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));

        if (semester.getSubjects() != null && !semester.getSubjects().isEmpty()) {
            throw new CustomException(ErrorCode.SEMESTER_HAS_SUBJECTS);
        }

        semesterRepository.delete(semester);
    }
}