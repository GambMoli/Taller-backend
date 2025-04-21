package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.forum.ForumDTO;
import co.edu.udes.backend.dto.forum.ForumRequestDTO;
import co.edu.udes.backend.service.ForumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forums")
@RequiredArgsConstructor
public class ForumController {

    private final ForumService forumService;

    @PostMapping
    public ResponseEntity<ForumDTO> createForum(@RequestBody ForumRequestDTO request) {
        return ResponseEntity.ok(forumService.createForum(request));
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ForumDTO>> getForumsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(forumService.getForumsByGroup(groupId));
    }

    @GetMapping("/{forumId}")
    public ResponseEntity<ForumDTO> getForumById(@PathVariable Long forumId) {
        return ResponseEntity.ok(forumService.getForumById(forumId));
    }

    @DeleteMapping("/{forumId}")
    public ResponseEntity<Void> deleteForum(@PathVariable Long forumId) {
        forumService.deleteForum(forumId);
        return ResponseEntity.noContent().build();
    }
}
