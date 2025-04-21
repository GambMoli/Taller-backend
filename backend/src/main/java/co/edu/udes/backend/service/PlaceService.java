package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.place.PlaceDTO;
import co.edu.udes.backend.dto.place.PlaceResponseDTO;
import co.edu.udes.backend.mappers.place.PlaceMapper;
import co.edu.udes.backend.models.Place;
import co.edu.udes.backend.repositories.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    public PlaceService(PlaceRepository placeRepository, PlaceMapper placeMapper){
        this.placeRepository=placeRepository;
        this.placeMapper=placeMapper;
    }

    public List<PlaceResponseDTO> getAlls(){
        return placeRepository.findAll().stream()
                .map(placeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    public PlaceResponseDTO getOne(long Id){
        Place place = placeRepository.findById(Id).orElseThrow(() -> new RuntimeException("Reserva no encontrada con id: " + Id));
        return placeMapper.toResponseDTO(place);
    }

    public PlaceResponseDTO createPlace(PlaceDTO placeDTO) {
        if (placeRepository.existsByName(placeDTO.getName())) {
            throw new RuntimeException("Ya existe un espacio con ese nombre");
        }

        Place place = placeMapper.toEntity(placeDTO);
        Place placeSaved = placeRepository.save(place);
        return placeMapper.toResponseDTO(placeSaved);
    }

    public  PlaceResponseDTO modifyPlace(long Id,PlaceDTO placeDTO){
        Place ExistPlace = placeRepository.findById(Id).orElseThrow(()->new RuntimeException("Reserva no encontrada con id: " + Id));
        if (!ExistPlace.getName().equals(placeDTO.getName())){
            placeRepository.existsByName(placeDTO.getName());
        }
        ExistPlace.setType(placeDTO.getType());
        ExistPlace.setName(placeDTO.getName());
        ExistPlace.setQuantity(placeDTO.getQuantity());
        ExistPlace.setDescription(placeDTO.getDescription());
        placeRepository.save(ExistPlace);
        return placeMapper.toResponseDTO(ExistPlace);
    }

    public  void deletePlace(long id){
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El sitio no existe"));
        placeRepository.delete(place);
    }
}
