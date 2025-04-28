package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.notification.NotificationDTO;
import co.edu.udes.backend.dto.notification.NotificationRequestDTO;
import co.edu.udes.backend.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationRequestDTO requestDTO) {
        return new ResponseEntity<>(notificationService.createNotification(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(notificationService.getNotificationsByGroup(groupId));
    }

    @GetMapping("/group/{groupId}/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotificationsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsByGroup(groupId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(notificationService.getNotificationsByTeacher(teacherId));
    }

    @PutMapping("/{notificationId}/mark-read")
    public ResponseEntity<NotificationDTO> markAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
}