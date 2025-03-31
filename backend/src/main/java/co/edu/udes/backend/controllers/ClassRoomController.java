package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.ClassRoom;
import co.edu.udes.backend.repositories.ClassRoomRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/classrooms")
public class ClassRoomController {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    // Devolver un aula
    @PutMapping("/{id}/return")
    public ResponseEntity<ClassRoom> returnClassRoom(@PathVariable Long id) {
        ClassRoom classRoom = classRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ClassRoom not found with id: " + id));

        classRoom.returnResource();
        classRoomRepository.save(classRoom);
        return ResponseEntity.ok(classRoom);
    }
}
