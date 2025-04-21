package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.reserve.ReserveDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
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
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + id));
        return reserveMapper.toResponseDTO(reserve);
    }

    public ReserveResponseDTO createReserve(ReserveDTO reserveDTO) {
        // Validar código único
        if (reserveRepository.existsByCode(reserveDTO.getCode())) {
            throw new RuntimeException("Ya existe una reserva con este código.");
        }

        // Mapear DTO a entidad
        Reserve reserve = reserveMapper.toEntity(reserveDTO);

        // Establecer fecha de reserva si no está presente
        if (reserveDTO.getReserveDate() == null) {
            throw new IllegalArgumentException("La fecha de la reserva no puede ser nula.");
        }
        reserve.setReserveDate(reserveDTO.getReserveDate());

        // Debug (opcional)
        System.out.println("ReserveDTO fecha: " + reserveDTO.getReserveDate());
        System.out.println("Reserve fecha después de mapeo: " + reserve.getReserveDate());

        // Verificar si ya existe una reserva para ese lugar, hora y fecha
        if (reserveRepository.existsByPlaceAndHourInitAndReserveDate(
                reserve.getPlace(),
                reserve.getHourInit(),
                reserve.getReserveDate())) {
            throw new IllegalArgumentException("Ya existe una reserva para ese lugar, hora y fecha.");
        }

        // Guardar la reserva
        Reserve saveReserve = reserveRepository.save(reserve);

        // Retornar DTO de respuesta
        return reserveMapper.toResponseDTO(saveReserve);
    }



    public void deleteReserve(long Id){
        if (!reserveRepository.existsById(Id)){
            throw new RuntimeException("La reserva no existe");

        }
        reserveRepository.deleteById(Id);
    }



}
