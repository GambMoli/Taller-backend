package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.career.CareerDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.career.CareerMapper;
import co.edu.udes.backend.models.Career;
import co.edu.udes.backend.repositories.CareerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CareerService {
    private final CareerRepository careerRepository;
    private final CareerMapper careerMapper;

    public CareerService(CareerRepository careerRepository, CareerMapper careerMapper) {
        this.careerRepository = careerRepository;
        this.careerMapper = careerMapper;
    }

    public Career create(Career career) {
        if (careerRepository.existsByName(career.getName())) {
            throw new CustomException(ErrorCode.CAREER_ALREADY_EXISTS);
        }
        return careerRepository.save(career);
    }

    public List<CareerDTO> findAll() {
        return careerRepository.findAll().stream()
                .map(careerMapper::toDto)
                .collect(Collectors.toList());
    }

    public CareerDTO findById(Long id) {
        Career career = careerRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.CAREER_NOT_FOUND));
        return careerMapper.toDto(career);
    }

    public CareerDTO update(Long id, Career career) {

        CareerDTO existing = findById(id);
        if (!existing.getName().equals(career.getName()) && careerRepository.existsByName(career.getName())) {
            throw new CustomException(ErrorCode.CAREER_ALREADY_EXISTS);
        }

        Career careerUpdate = careerMapper.toEntity(existing);
        careerRepository.save(careerUpdate);

        return careerMapper.toDto(careerUpdate);
    }

    public void delete(Long id) {
        if (!careerRepository.existsById(id)) {
            throw new CustomException(ErrorCode.CAREER_NOT_FOUND);
        }
        careerRepository.deleteById(id);
    }

}
