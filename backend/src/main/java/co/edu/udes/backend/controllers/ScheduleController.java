package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.schedule.ScheduleDTO;
import co.edu.udes.backend.dto.schedule.ScheduleResponseDTO;
import co.edu.udes.backend.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDTO> createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        ScheduleResponseDTO created = scheduleService.create(scheduleDTO);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDTO>> getAllSchedules() {
        List<ScheduleResponseDTO> schedules = scheduleService.findAll();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> getScheduleById(@PathVariable Long id) {
        ScheduleResponseDTO schedule = scheduleService.findById(id);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<Set<ScheduleResponseDTO>> getSchedulesByGroup(@PathVariable Long groupId) {
        Set<ScheduleResponseDTO> schedules = scheduleService.findByGroupId(groupId);
        return ResponseEntity.ok(schedules);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDTO> updateSchedule(@PathVariable Long id, @RequestBody ScheduleDTO scheduleDTO) {
        ScheduleResponseDTO updated = scheduleService.update(id, scheduleDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
