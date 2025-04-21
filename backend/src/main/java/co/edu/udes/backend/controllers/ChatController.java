package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.chat.ChatDTO;
import co.edu.udes.backend.dto.chat.ChatRequestDTO;
import co.edu.udes.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatRequestDTO chatRequest) {
        return ResponseEntity.ok(chatService.createChat(chatRequest));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ChatDTO>> getChatsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(chatService.getChatsByParticipant(studentId));
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable Long chatId) {
        return ResponseEntity.ok(chatService.getChatById(chatId));
    }

    @DeleteMapping("/{chatId}")
    public ResponseEntity<Void> deleteChat(@PathVariable Long chatId) {
        chatService.deleteChat(chatId);
        return ResponseEntity.noContent().build();
    }
}