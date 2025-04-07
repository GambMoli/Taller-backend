package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.career.CareerDTO;
import co.edu.udes.backend.dto.career.CareerSimpleDTO;
import co.edu.udes.backend.dto.semester.SemesterDTO;
import co.edu.udes.backend.dto.subject.SubjectDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.Career;
import co.edu.udes.backend.repositories.CareerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerService {
    private final CareerRepository careerRepository;

    public CareerService(CareerRepository careerRepository) {
        this.careerRepository = careerRepository;
    }

    public Career create(Career career) {
        if (careerRepository.existsByName(career.getName())) {
            throw new CustomException(ErrorCode.CAREER_ALREADY_EXISTS);
        }
        return careerRepository.save(career);
    }

    public List<CareerDTO> findAll() {
        return careerRepository.findAll().stream()
                .map(this::convertToDTO)  // Convierte usando CareerDTO
                .collect(Collectors.toList());
    }

    private CareerDTO convertToDTO(Career career) {
        CareerDTO dto = new CareerDTO();
        dto.setId(career.getId());
        dto.setName(career.getName());


        // Semesters
        dto.setSemesters(career.getSemesters().stream()
                .map(semester -> {
                    SemesterDTO semesterDTO = new SemesterDTO();
                    semesterDTO.setId(semester.getId());
                    semesterDTO.setNumber(semester.getNumber());

                    semesterDTO.setSubjects(semester.getSubjects().stream()
                            .map(subject -> {
                                SubjectDTO subjectDTO = new SubjectDTO();
                                subjectDTO.setId(subject.getId());
                                subjectDTO.setName(subject.getName());
                                return subjectDTO;
                            }).collect(Collectors.toList()));

                    return semesterDTO;
                }).collect(Collectors.toList()));

        return dto;
    }

    public List<CareerSimpleDTO> findAllSimple() {
        return careerRepository.findAll().stream()
                .map(CareerSimpleDTO::fromEntity)  // Usamos CareerSimpleDTO para mostrar solo el nombre y ID
                .collect(Collectors.toList());
    }

    public Career findById(Long id) {
        return careerRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CAREER_NOT_FOUND));
    }

    public CareerDTO findDTOById(Long id) {
        Career career = findById(id);
        return convertToDTO(career);  // Devuelve el DTO completo
    }

    public Career update(Long id, Career career) {
        Career existing = findById(id);

        // Verificar si el nuevo nombre ya existe en otra carrera
        if (!existing.getName().equals(career.getName()) && careerRepository.existsByName(career.getName())) {
            throw new CustomException(ErrorCode.CAREER_ALREADY_EXISTS);
        }

        existing.setName(career.getName());
        existing.setSubjects(career.getSubjects());
        return careerRepository.save(existing);
    }

    public void delete(Long id) {
        if (!careerRepository.existsById(id)) {
            throw new CustomException(ErrorCode.CAREER_NOT_FOUND);
        }
        careerRepository.deleteById(id);
    }
}
