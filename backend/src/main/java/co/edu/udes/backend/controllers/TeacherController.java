package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.groups.GroupClassResponseDTO;
import co.edu.udes.backend.dto.reserve.ReserveResponseDTO;
import co.edu.udes.backend.dto.teacher.*;
import co.edu.udes.backend.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {
    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<TeacherResponseDTO> create(@RequestBody TeacherDTO teacherDTO) {
        return new ResponseEntity<>(teacherService.create(teacherDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TeacherResponseDTO>> findAll() {
        return ResponseEntity.ok(teacherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherResponseDTO> update(@PathVariable Long id, @RequestBody TeacherDTO teacherDTO) {
        return ResponseEntity.ok(teacherService.update(id, teacherDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/assign-group")
    public ResponseEntity<TeacherResponseDTO> assignGroup(@RequestBody GroupAssignmentDTO assignmentDTO) {
        return ResponseEntity.ok(teacherService.assignGroup(assignmentDTO));
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<TeacherScheduleDTO> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getSchedule(id));
    }
    @GetMapping("/{teacherId}/my_reserve")
    public ResponseEntity<List<ReserveResponseDTO>>getReserve(@PathVariable long teacherId){
        return ResponseEntity.ok(teacherService.getReservesByTeacher(teacherId));

    }

    @GetMapping("/{teacherId}/myGroups")
    public ResponseEntity<List<GroupClassResponseDTO>> getGroups(@PathVariable("teacherId") long teacherID) {
        return ResponseEntity.ok(teacherService.getGroupByTeacher(teacherID));
    }
}