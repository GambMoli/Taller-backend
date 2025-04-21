package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.message.MessageDTO;
import co.edu.udes.backend.dto.message.MessageRequestDTO;
import co.edu.udes.backend.models.*;
import co.edu.udes.backend.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public MessageDTO sendMessage(MessageRequestDTO messageRequest) {
        Chat chat = chatRepository.findById(messageRequest.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        Student sender = studentRepository.findById(messageRequest.getSenderId())
                .orElseThrow(() -> new RuntimeException("Estudiante remitente no encontrado"));

        Message message = new Message();
        message.setTitle(messageRequest.getTitle());
        message.setBody(messageRequest.getBody());
        message.setSender(sender);
        message.setChat(chat);

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }

    public List<MessageDTO> getMessagesByChat(Long chatId) {
        return messageRepository.findByChatIdOrderByDateAsc(chatId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessagesBySender(Long senderId) {
        return messageRepository.findBySenderIdOrderByDateDesc(senderId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }

    private MessageDTO convertToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setTitle(message.getTitle());
        dto.setBody(message.getBody());
        dto.setDate(message.getDate());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getName());
        dto.setChatId(message.getChat().getId());
        return dto;
    }
}