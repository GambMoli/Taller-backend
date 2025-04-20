package co.edu.udes.backend.mappers.period;

import co.edu.udes.backend.dto.period.PeriodCreateDTO;
import co.edu.udes.backend.dto.period.PeriodDTO;
import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import co.edu.udes.backend.models.Period;
import co.edu.udes.backend.models.Semester;
import co.edu.udes.backend.repositories.SemesterRepository;
import org.springframework.stereotype.Component;

@Component
public class PeriodMapper {
    private final SemesterRepository semesterRepository;

    public PeriodMapper(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public Period toEntity(PeriodCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        Period period = new Period();
        period.setName(dto.getName());
        period.setStartDate(dto.getStartDate());
        period.setEndDate(dto.getEndDate());
        period.setWeight(dto.getWeight());

        if (dto.getSemesterId() != null) {
            Semester semester = semesterRepository.findById(dto.getSemesterId()).orElse(null);
            period.setSemester(semester);
        }

        return period;
    }

    public PeriodDTO toDTO(Period entity) {
        if (entity == null) {
            return null;
        }

        PeriodDTO dto = new PeriodDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setWeight(entity.getWeight());

        if (entity.getSemester() != null) {
            dto.setSemesterId(entity.getSemester().getId());
        }

        return dto;
    }

    public PeriodResponseDTO toResponseDTO(Period entity) {
        if (entity == null) {
            return null;
        }

        PeriodResponseDTO dto = new PeriodResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setWeight(entity.getWeight());

        if (entity.getSemester() != null) {
            dto.setSemesterId(entity.getSemester().getId());
            dto.setSemesterName("Semestre " + entity.getSemester().getNumber());
        }

        return dto;
    }
}