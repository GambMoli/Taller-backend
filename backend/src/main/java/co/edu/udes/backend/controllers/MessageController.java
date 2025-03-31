package co.edu.udes.backend.controllers;

import co.edu.udes.backend.models.Message;
import co.edu.udes.backend.models.User;
import co.edu.udes.backend.repositories.MessageRepository;
import co.edu.udes.backend.repositories.UserRepository;
import co.edu.udes.backend.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    // Enviar mensaje
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestParam Long userId, @RequestParam String body, @RequestParam String title) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Message message = new Message();
        message.setTransmitter(user);
        message.setBody(body);
        message.setTitle(title);
        message.setDate(LocalDateTime.now());

        messageRepository.save(message);
        return ResponseEntity.ok(message);
    }

    // Editar mensaje
    @PutMapping("/{id}")
    public ResponseEntity<Message> editMessage(@PathVariable Long id, @RequestParam String newBody, @RequestParam String newTitle) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));

        messageRepository.save(message);
        return ResponseEntity.ok(message);
    }

    // Eliminar mensaje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));


        messageRepository.delete(message);
        return ResponseEntity.noContent().build();
    }
}
