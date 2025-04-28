package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.message.MessageDTO;
import co.edu.udes.backend.dto.message.MessageRequestDTO;
import co.edu.udes.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageRequestDTO messageRequest) {
        return ResponseEntity.ok(messageService.sendMessage(messageRequest));
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByChat(@PathVariable Long chatId) {
        return ResponseEntity.ok(messageService.getMessagesByChat(chatId));
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<MessageDTO>> getMessagesBySender(@PathVariable Long senderId) {
        return ResponseEntity.ok(messageService.getMessagesBySender(senderId));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}