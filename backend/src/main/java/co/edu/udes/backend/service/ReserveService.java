package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.reserve.ReserveDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.mappers.reserve.ReserveMapper;
import co.edu.udes.backend.models.Place;
import co.edu.udes.backend.models.Reserve;
import co.edu.udes.backend.repositories.ReserveRepository;


import org.springframework.stereotype.Service;


import java.time.LocalDate;

import java.util.List;

import java.util.stream.Collectors;

@Service
public class ReserveService {

    private final ReserveRepository reserveRepository;

    private final ReserveMapper reserveMapper;



    public  ReserveService( ReserveMapper reserveMapper, ReserveRepository reserveRepository){
        this.reserveRepository=reserveRepository;
        this.reserveMapper=reserveMapper;

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


        Reserve reserve = reserveMapper.toEntity(reserveDTO);


        if (reserveDTO.getReserveDate() == null) {
            throw new CustomException(ErrorCode.DATE_OF_RESERVE);
        }
        reserve.setReserveDate(reserveDTO.getReserveDate());


        if (reserveRepository.existsByPlaceAndHourInitAndReserveDate(
                reserve.getPlace(),
                reserve.getHourInit(),
                reserve.getReserveDate())) {
            throw new CustomException(ErrorCode.RESERVE_EXISTS);
        }


        Reserve saveReserve = reserveRepository.save(reserve);


        return reserveMapper.toResponseDTO(saveReserve);
    }



    public void deleteReserve(long Id){
        if (!reserveRepository.existsById(Id)){
            throw new CustomException(ErrorCode.RESERVE_NOT_FOUND);

        }
        reserveRepository.deleteById(Id);
    }



}
