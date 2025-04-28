package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.message.MessageDTO;
import co.edu.udes.backend.dto.message.MessageRequestDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
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
    private final ForumRepository forumRepository;

    @Transactional
    public MessageDTO sendMessage(MessageRequestDTO messageRequest) {
        Student sender = studentRepository.findById(messageRequest.getSenderId())
                .orElseThrow(() -> new CustomException(ErrorCode.STUDENT_NOT_FOUND));

        boolean hasChat = messageRequest.getChatId() != null;
        boolean hasForum = messageRequest.getForumId() != null;

        if (hasChat == hasForum) {
            throw new CustomException(ErrorCode.INVALID_MESSAGE_TARGET);
        }

        Message message = new Message();
        message.setTitle(messageRequest.getTitle());
        message.setBody(messageRequest.getBody());
        message.setSender(sender);

        if (hasChat) {
            Chat chat = chatRepository.findById(messageRequest.getChatId())
                    .orElseThrow(() -> new CustomException(ErrorCode.CHAT_NOT_FOUND));

            if (!isParticipant(chat, sender.getId())) {
                throw new CustomException(ErrorCode.STUDENT_NOT_IN_CHAT);
            }

            message.setChat(chat);
        } else {
            Forum forum = forumRepository.findById(messageRequest.getForumId())
                    .orElseThrow(() -> new CustomException(ErrorCode.FORUM_NOT_FOUND));

            if (!isInForumGroup(forum, sender.getId())) {
                throw new CustomException(ErrorCode.STUDENT_NOT_IN_FORUM_GROUP);
            }

            message.setForum(forum);
        }

        Message savedMessage = messageRepository.save(message);
        return convertToDTO(savedMessage);
    }


    private boolean isParticipant(Chat chat, Long studentId) {
        return chat.getParticipant1().getId().equals(studentId) ||
                chat.getParticipant2().getId().equals(studentId);
    }

    private boolean isInForumGroup(Forum forum, Long studentId) {
        return forum.getGroupClass().getStudents().stream()
                .anyMatch(student -> student.getId().equals(studentId));
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

        if (message.getChat() != null) {
            dto.setChatId(message.getChat().getId());
        }

        if (message.getForum() != null) {
            dto.setForumId(message.getForum().getId());
        }

        return dto;
    }


}