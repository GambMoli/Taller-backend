package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.period.PeriodCreateDTO;
import co.edu.udes.backend.dto.period.PeriodResponseDTO;
import co.edu.udes.backend.dto.period.PeriodUpdateDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.period.PeriodMapper;
import co.edu.udes.backend.models.Period;
import co.edu.udes.backend.models.Semester;
import co.edu.udes.backend.repositories.PeriodRepository;
import co.edu.udes.backend.repositories.SemesterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeriodService {
    private final PeriodRepository periodRepository;
    private final SemesterRepository semesterRepository;
    private final PeriodMapper periodMapper;

    public PeriodService(PeriodRepository periodRepository,
                         SemesterRepository semesterRepository,
                         PeriodMapper periodMapper) {
        this.periodRepository = periodRepository;
        this.semesterRepository = semesterRepository;
        this.periodMapper = periodMapper;
    }

    public PeriodResponseDTO create(PeriodCreateDTO dto) {
        Semester semester = semesterRepository.findById(dto.getSemesterId())
                .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));

        // Validate period dates are within semester dates
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            if (dto.getStartDate().isBefore(semester.getStartDate()) ||
                    dto.getEndDate().isAfter(semester.getEndDate())) {
                throw new CustomException(ErrorCode.INVALID_PERIOD_DATES);
            }
        }

        Period period = periodMapper.toEntity(dto);
        Period savedPeriod = periodRepository.save(period);
        return periodMapper.toResponseDTO(savedPeriod);
    }

    public List<PeriodResponseDTO> getAllBySemesterId(Long semesterId) {
        if (!semesterRepository.existsById(semesterId)) {
            throw new CustomException(ErrorCode.SEMESTER_NOT_FOUND);
        }

        return periodRepository.findBySemesterIdOrderByStartDateAsc(semesterId).stream()
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

        // Update fields
        if (dto.getName() != null) {
            existingPeriod.setName(dto.getName());
        }
        if (dto.getStartDate() != null) {
            existingPeriod.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            existingPeriod.setEndDate(dto.getEndDate());
        }
        if (dto.getWeight() != null) {
            existingPeriod.setWeight(dto.getWeight());
        }

        // Validate updated dates are within semester dates
        Semester semester = existingPeriod.getSemester();
        if (existingPeriod.getStartDate() != null && existingPeriod.getEndDate() != null) {
            if (existingPeriod.getStartDate().isBefore(semester.getStartDate()) ||
                    existingPeriod.getEndDate().isAfter(semester.getEndDate())) {
                throw new CustomException(ErrorCode.INVALID_PERIOD_DATES);
            }
        }

        Period updatedPeriod = periodRepository.save(existingPeriod);
        return periodMapper.toResponseDTO(updatedPeriod);
    }

    public void delete(Long id) {
        Period period = periodRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PERIOD_NOT_FOUND));

        periodRepository.delete(period);
    }

    @Transactional
    public void generatePeriodsForSemester(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEMESTER_NOT_FOUND));

        // Make sure the semester has start and end dates
        if (semester.getStartDate() == null || semester.getEndDate() == null) {
            throw new CustomException(ErrorCode.SEMESTER_DATES_REQUIRED);
        }

        // Calculate total days in semester
        long totalDays = ChronoUnit.DAYS.between(semester.getStartDate(), semester.getEndDate());
        long periodDays = totalDays / 3; // 3 periods

        LocalDate periodStart = semester.getStartDate();

        // Delete existing periods if any
        periodRepository.deleteBySemesterId(semesterId);

        // Create 3 periods with equal weights
        for (int i = 1; i <= 3; i++) {
            Period period = new Period();
            period.setName("Corte " + i);
            period.setSemester(semester);
            period.setWeight(0.33); // Approximately 33% each (will need manual adjustment for exactness)
            period.setStartDate(periodStart);

            // Calculate end date for this period
            LocalDate periodEnd;
            if (i == 3) {
                // Last period ends with semester
                periodEnd = semester.getEndDate();
            } else {
                periodEnd = periodStart.plusDays(periodDays - 1);
            }

            period.setEndDate(periodEnd);
            periodRepository.save(period);

            // Next period starts the day after
            periodStart = periodEnd.plusDays(1);
        }
    }
}