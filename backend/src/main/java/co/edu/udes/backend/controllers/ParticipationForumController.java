package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.ParticipationForum;
import co.edu.udes.backend.models.User;
import co.edu.udes.backend.repositories.ParticipationForumRepository;
import co.edu.udes.backend.repositories.UserRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/participation-forums")
public class ParticipationForumController {

    @Autowired
    private ParticipationForumRepository participationForumRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ParticipationForum> createParticipation(@RequestParam Long userId, @RequestParam Long forumId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        ParticipationForum participationForum = new ParticipationForum();
        participationForum.setUser(user);
        participationForum.setEntryDate(LocalDateTime.now());

        participationForumRepository.save(participationForum);
        return ResponseEntity.ok(participationForum);
    }
}
