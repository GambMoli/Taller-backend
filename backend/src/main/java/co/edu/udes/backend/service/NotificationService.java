package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.notification.NotificationDTO;
import co.edu.udes.backend.dto.notification.NotificationRequestDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Notification;
import co.edu.udes.backend.models.Teacher;
import co.edu.udes.backend.repositories.GroupClassRepository;
import co.edu.udes.backend.repositories.NotificationRepository;
import co.edu.udes.backend.repositories.TeacherRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final TeacherRepository teacherRepository;
    private final GroupClassRepository groupClassRepository;

    @Transactional
    public NotificationDTO createNotification(NotificationRequestDTO requestDTO) {
        Teacher teacher = teacherRepository.findById(requestDTO.getTeacherId())
                .orElseThrow(() -> new CustomException(ErrorCode.TEACHER_NOT_FOUND));

        GroupClass groupClass = groupClassRepository.findById(requestDTO.getGroupClassId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        if (!isTeacherAssignedToGroup(teacher.getId(), groupClass.getId())) {
            throw new CustomException(ErrorCode.TEACHER_NOT_ASSIGNED_TO_GROUP);
        }

        Notification notification = new Notification();
        notification.setTitle(requestDTO.getTitle());
        notification.setContent(requestDTO.getContent());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.setSender(teacher);
        notification.setGroupClass(groupClass);

        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    public List<NotificationDTO> getNotificationsByGroup(Long groupId) {
        return notificationRepository.findByGroupClassIdOrderByCreatedAtDesc(groupId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadNotificationsByGroup(Long groupId) {
        return notificationRepository.findByGroupClassIdAndIsReadOrderByCreatedAtDesc(groupId, false).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByTeacher(Long teacherId) {
        return notificationRepository.findBySenderIdOrderByCreatedAtDesc(teacherId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationDTO markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notification.setRead(true);
        return convertToDTO(notificationRepository.save(notification));
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new CustomException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notificationRepository.deleteById(notificationId);
    }

    public long countUnreadNotificationsByGroup(Long groupId) {
        return notificationRepository.countByGroupClassIdAndIsRead(groupId, false);
    }

    private boolean isTeacherAssignedToGroup(Long teacherId, Long groupId) {
        GroupClass group = groupClassRepository.findById(groupId).orElse(null);
        return group != null && group.getTeacher() != null &&
                group.getTeacher().getId().equals(teacherId);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setContent(notification.getContent());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setRead(notification.isRead());
        dto.setSenderId(notification.getSender().getId());
        dto.setSenderName(notification.getSender().getName());
        dto.setGroupClassId(notification.getGroupClass().getId());
        dto.setGroupClassName(notification.getGroupClass().getName());
        return dto;
    }
}