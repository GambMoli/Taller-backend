package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Forum;
import co.edu.udes.backend.models.User;
import co.edu.udes.backend.repositories.ForumRepository;
import co.edu.udes.backend.repositories.UserRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/forums")
public class ForumController {

    @Autowired
    private ForumRepository forumRepository;

    @Autowired
    private UserRepository userRepository;

    // Crear foro
    @PostMapping
    public ResponseEntity<Forum> createForum(@RequestParam Long userId, @RequestParam String theme) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Forum forum = new Forum();

        forumRepository.save(forum);
        return ResponseEntity.ok(forum);
    }
}
