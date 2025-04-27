package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.chat.ChatDTO;
import co.edu.udes.backend.dto.chat.ChatRequestDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.Chat;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.repositories.ChatRepository;
import co.edu.udes.backend.repositories.StudentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public ChatDTO createChat(ChatRequestDTO chatRequest) {
        Student participant1 = studentRepository.findById(chatRequest.getParticipant1Id())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        Student participant2 = studentRepository.findById(chatRequest.getParticipant2Id())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        Chat chat = new Chat();
        chat.setParticipant1(participant1);
        chat.setParticipant2(participant2);
        chat.setCreationDate(LocalDateTime.now());

        Chat savedChat = chatRepository.save(chat);
        return convertToDTO(savedChat);
    }

    public List<ChatDTO> getChatsByParticipant(Long studentId) {
        return chatRepository.findByParticipantId(studentId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ChatDTO getChatById(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));
        return convertToDTO(chat);
    }

    @Transactional
    public void deleteChat(Long chatId) {
        chatRepository.deleteById(chatId);
    }

    private ChatDTO convertToDTO(Chat chat) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setCreationDate(chat.getCreationDate());
        dto.setParticipant1Id(chat.getParticipant1().getId());
        dto.setParticipant1Name(chat.getParticipant1().getName());
        dto.setParticipant2Id(chat.getParticipant2().getId());
        dto.setParticipant2Name(chat.getParticipant2().getName());
        return dto;
    }
}