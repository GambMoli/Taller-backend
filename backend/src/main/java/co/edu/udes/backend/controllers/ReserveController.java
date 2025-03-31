package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Reserve;
import co.edu.udes.backend.models.User;
import co.edu.udes.backend.repositories.ReserveRepository;
import co.edu.udes.backend.repositories.UserRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reserves")
public class ReserveController {

    @Autowired
    private ReserveRepository reserveRepository;

    @Autowired
    private UserRepository userRepository;

    // Crear una nueva reserva
    @PostMapping
    public ResponseEntity<Reserve> createReserve(@RequestParam Long userId, @RequestParam String date) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        LocalDateTime reserveDate = LocalDateTime.parse(date);
        Reserve reserve = new Reserve();
        reserve.createReserve(user, reserveDate);

        reserveRepository.save(reserve);
        return ResponseEntity.ok(reserve);
    }

    // Habilitar una reserva (cambiar su estado a confirmado)
    @PutMapping("/{id}/enable")
    public ResponseEntity<Reserve> enableReserve(@PathVariable Long id) {
        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserve not found with id: " + id));

        reserve.enableReserve();
        reserveRepository.save(reserve);
        return ResponseEntity.ok(reserve);
    }

    // Obtener todas las reservas
    @GetMapping
    public List<Reserve> getAllReserves() {
        return reserveRepository.findAll();
    }
}
