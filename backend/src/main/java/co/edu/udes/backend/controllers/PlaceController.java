package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.place.PlaceDTO;
import co.edu.udes.backend.dto.place.PlaceResponseDTO;
import co.edu.udes.backend.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/place")
public class PlaceController {
    private final PlaceService placeService;

    public  PlaceController (PlaceService placeService)
    {
        this.placeService=placeService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceResponseDTO>> getAlls(){
        return ResponseEntity.ok(placeService.getAlls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponseDTO> getOne(@PathVariable long id){
        return ResponseEntity.ok(placeService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<PlaceResponseDTO> createPlace(@RequestBody PlaceDTO placeDTO){
        return ResponseEntity.ok(placeService.createPlace(placeDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaceResponseDTO> modifyPlace(@PathVariable long id,@RequestBody PlaceDTO placeDTO){
        return ResponseEntity.ok(placeService.modifyPlace(id,placeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlaceResponseDTO> deletePlace(@PathVariable long id){
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
