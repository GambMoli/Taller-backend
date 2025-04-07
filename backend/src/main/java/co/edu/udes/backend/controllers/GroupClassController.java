package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.groups.GroupClassDTO;
import co.edu.udes.backend.dto.groups.GroupClassResponseDTO;
import co.edu.udes.backend.service.GroupClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupClassController {
    private final GroupClassService groupClassService;

    public GroupClassController(GroupClassService groupClassService) {
        this.groupClassService = groupClassService;
    }

    @PostMapping
    public GroupClassResponseDTO create(@RequestBody GroupClassDTO groupClassDTO) {
        return groupClassService.create(groupClassDTO);
    }

    @GetMapping
    public List<GroupClassResponseDTO> findAll() {
        return groupClassService.findAll();
    }

    @GetMapping("/{id}")
    public GroupClassResponseDTO findById(@PathVariable Long id) {
        return groupClassService.findById(id);
    }

    @PutMapping("/{id}")
    public GroupClassResponseDTO update(@PathVariable Long id, @RequestBody GroupClassDTO groupClassDTO) {
        return groupClassService.update(id, groupClassDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        groupClassService.delete(id);
    }
}