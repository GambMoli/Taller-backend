package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.reserve.ReserveDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.dto.reserve.ReserveTimeUpdateDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.reserve.ReserveMapper;
import co.edu.udes.backend.models.Place;
import co.edu.udes.backend.models.Reserve;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import co.edu.udes.backend.repositories.PlaceRepository;
import co.edu.udes.backend.repositories.ReserveRepository;


import co.edu.udes.backend.repositories.StudentRepository;
import co.edu.udes.backend.repositories.TeacherRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDate;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class ReserveService {
    private final PlaceRepository placeRepository;

    private final ReserveRepository reserveRepository;

    private final ReserveMapper reserveMapper;

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;


    public  ReserveService(StudentRepository studentRepository,TeacherRepository teacherRepository,PlaceRepository placeRepository,ReserveMapper reserveMapper, ReserveRepository reserveRepository){
        this.reserveRepository=reserveRepository;
        this.reserveMapper=reserveMapper;
        this.placeRepository=placeRepository;
        this.teacherRepository=teacherRepository;
        this.studentRepository=studentRepository;
    }

    public List<ReserveResponseDTO> getalls(){
        return reserveRepository.findAll().stream()
                .map(reserveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ReserveResponseDTO getOneReserve(long id) {
        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVE_NOT_FOUND));
        return reserveMapper.toResponseDTO(reserve);
    }

    public ReserveResponseDTO createReserve(ReserveDTO reserveDTO) {
        if (reserveRepository.existsByCode(reserveDTO.getCode())) {
            throw new CustomException(ErrorCode.RESERVECODE_EXISTS);
        }

        if (reserveDTO.getReserveDate() == null) {
            throw new CustomException(ErrorCode.DATE_OF_RESERVE);
        }

        // Obtener objeto Place
        Place place = placeRepository.findById(reserveDTO.getPlaceId().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PLACE_NOT_FOUND));
        // Obtener Student (opcional)
        Student student = null;
        if (reserveDTO.getStudentId() != null && reserveDTO.getStudentId().getId() != null) {
            student = studentRepository.findById(reserveDTO.getStudentId().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));
        }

        // Obtener Teacher (opcional)
        Teacher teacher = null;
        if (reserveDTO.getTeacherId() != null && reserveDTO.getTeacherId().getId() != null) {
            teacher = teacherRepository.findById(reserveDTO.getTeacherId().getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));
        }
        // Mapear DTO + Place a la entidad Reserve
        Reserve reserve = reserveMapper.toEntity(reserveDTO, place,student,teacher );
        reserve.setReserveDate(reserveDTO.getReserveDate());

        // Validar reserva existente
        if (reserveRepository.existsByPlaceAndHourInitAndReserveDate(
                reserve.getPlace(),
                reserve.getHourInit(),
                reserve.getReserveDate())) {
            throw new CustomException(ErrorCode.RESERVE_EXISTS);
        }

        Reserve saveReserve = reserveRepository.save(reserve);

        return reserveMapper.toResponseDTO(saveReserve);
    }

    public ReserveResponseDTO updateReserveTime(Long id, ReserveTimeUpdateDTO dto) {
        // Buscar reserva existente
        Reserve existingReserve = reserveRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVE_NOT_FOUND));

        // Validar fecha obligatoria
        if (dto.getReserveDate() == null) {
            throw new CustomException(ErrorCode.DATE_OF_RESERVE);
        }

        // Validar conflicto con otra reserva (misma hora y fecha, mismo lugar, excepto esta)
        boolean reserveExists = reserveRepository.existsByPlaceAndHourInitAndReserveDateAndIdNot(
                existingReserve.getPlace(),
                dto.getHourInit(),
                dto.getReserveDate(),
                id
        );

        if (reserveExists) {
            throw new CustomException(ErrorCode.RESERVE_EXISTS);
        }

        // Actualizar fecha y horas
        existingReserve.setReserveDate(dto.getReserveDate());
        existingReserve.setHourInit(dto.getHourInit());
        existingReserve.setHourFinish(dto.getHourFinish());

        // Guardar y retornar
        Reserve updated = reserveRepository.save(existingReserve);
        return reserveMapper.toResponseDTO(updated);
    }





    public void deleteReserve(long Id){
        if (!reserveRepository.existsById(Id)){
            throw new CustomException(ErrorCode.RESERVE_NOT_FOUND);

        }
        reserveRepository.deleteById(Id);
    }



}
