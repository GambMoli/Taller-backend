package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.reserve.ReserveDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.service.ReserveService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserve")
@CrossOrigin(origins = "*")
public class ReserveController {

    private final ReserveService reserveService;

    public ReserveController(ReserveService reserveService){
        this.reserveService= reserveService;
    }
    @GetMapping("/test")
    public String test() {
        return "Controlador funcionando";
    }
    @GetMapping
    public ResponseEntity<List<ReserveResponseDTO>> GetAlls(){
        return ResponseEntity.ok(reserveService.getalls());
    }

    @PostMapping
    public ResponseEntity<ReserveResponseDTO> CreateReserve(@RequestBody ReserveDTO reserveDTO){
        return ResponseEntity.ok(reserveService.createReserve(reserveDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReserveResponseDTO> GetOneReserve(@PathVariable long id){
        return ResponseEntity.ok(reserveService.getOneReserve(id));
    }



    @DeleteMapping("/{id}")
    public  ResponseEntity<ReserveResponseDTO> deleteReserve(@PathVariable long id){
        reserveService.deleteReserve(id);
        return ResponseEntity.noContent().build();
    }
}
