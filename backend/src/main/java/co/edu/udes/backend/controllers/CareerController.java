package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.career.CareerDTO;
import co.edu.udes.backend.models.Career;
import co.edu.udes.backend.service.CareerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
public class CareerController {
    private final CareerService careerService;

    public CareerController(CareerService careerService) {
        this.careerService = careerService;
    }

    @PostMapping
    public Career create(@RequestBody Career career) {
        return careerService.create(career);
    }

    @GetMapping
    public List<CareerDTO> findAll() {
        return careerService.findAll();
    }


    @GetMapping("/{id}")
    public CareerDTO findById(@PathVariable Long id) {
        return careerService.findById(id);
    }

    @PutMapping("/{id}")
    public CareerDTO update(@PathVariable Long id, @RequestBody Career career) {
        return careerService.update(id, career);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        careerService.delete(id);
    }
}
