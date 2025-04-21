package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.forum.ForumDTO;
import co.edu.udes.backend.dto.forum.ForumRequestDTO;
import co.edu.udes.backend.dto.message.MessageDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.Forum;
import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Message;
import co.edu.udes.backend.repositories.ForumRepository;
import co.edu.udes.backend.repositories.GroupClassRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumService {

    private final ForumRepository forumRepository;
    private final GroupClassRepository groupClassRepository;

    @Transactional
    public ForumDTO createForum(ForumRequestDTO request) {
        GroupClass groupClass = groupClassRepository.findById(request.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Forum forum = new Forum();
        forum.setTopic(request.getTopic());
        forum.setGroupClass(groupClass);

        Forum savedForum = forumRepository.save(forum);
        return convertToDTO(savedForum);
    }

    public List<ForumDTO> getForumsByGroup(Long groupId) {
        return forumRepository.findAll().stream()
                .filter(f -> f.getGroupClass().getId().equals(groupId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ForumDTO getForumById(Long forumId) {
        Forum forum = forumRepository.findById(forumId)
                .orElseThrow(() -> new CustomException(ErrorCode.FORUM_NOT_FOUND));
        return convertToDTO(forum);
    }

    @Transactional
    public void deleteForum(Long forumId) {
        forumRepository.deleteById(forumId);
    }

    private ForumDTO convertToDTO(Forum forum) {
        ForumDTO dto = new ForumDTO();
        dto.setId(forum.getId());
        dto.setTopic(forum.getTopic());
        dto.setGroupClassId(forum.getGroupClass().getId());
        dto.setGroupClassName(forum.getGroupClass().getName());

        if (forum.getMessages() != null) {
            List<MessageDTO> messages = forum.getMessages().stream()
                    .map(this::convertMessageToDTO)
                    .collect(Collectors.toList());
            dto.setMessages(messages);
        }

        return dto;
    }

    private MessageDTO convertMessageToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setBody(message.getBody());
        dto.setTitle(message.getTitle());
        dto.setDate(message.getDate());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getName());
        dto.setForumId(message.getForum() != null ? message.getForum().getId() : null);
        return dto;
    }
}
