package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Chat;
import co.edu.udes.backend.models.User;
import co.edu.udes.backend.repositories.ChatRepository;
import co.edu.udes.backend.repositories.UserRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    // Crear chat
    @PostMapping
    public ResponseEntity<Chat> createChat(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Chat chat = new Chat();

        chatRepository.save(chat);
        return ResponseEntity.ok(chat);
    }
}
