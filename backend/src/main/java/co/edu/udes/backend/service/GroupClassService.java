package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.groups.GroupClassDTO;
import co.edu.udes.backend.dto.groups.GroupClassResponseDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Subject;
import co.edu.udes.backend.repositories.GroupClassRepository;
import co.edu.udes.backend.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupClassService {
    private final GroupClassRepository groupClassRepository;
    private final SubjectRepository subjectRepository;

    public GroupClassService(GroupClassRepository groupClassRepository, SubjectRepository subjectRepository) {
        this.groupClassRepository = groupClassRepository;
        this.subjectRepository = subjectRepository;
    }

    public GroupClassResponseDTO create(GroupClassDTO groupClassDTO) {
        // Verificar que el subjectId no sea nulo
        if (groupClassDTO.getSubjectId() == null) {
            throw new CustomException(ErrorCode.SUBJECT_NOT_FOUND);
        }

        // Verificar que exista la materia
        Subject subject = subjectRepository.findById(groupClassDTO.getSubjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));

        // Verificar si ya existe un grupo con el mismo nombre para esta materia
        if (groupClassRepository.existsByNameAndSubjectId(groupClassDTO.getName(), groupClassDTO.getSubjectId())) {
            throw new CustomException(ErrorCode.GROUP_ALREADY_EXISTS);
        }

        GroupClass groupClass = new GroupClass();
        groupClass.setName(groupClassDTO.getName());
        groupClass.setCapacity(groupClassDTO.getCapacity());
        groupClass.setSubject(subject);

        GroupClass savedGroupClass = groupClassRepository.save(groupClass);
        return GroupClassResponseDTO.fromEntity(savedGroupClass);
    }

    public List<GroupClassResponseDTO> findAll() {
        return groupClassRepository.findAll().stream()
                .map(GroupClassResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public GroupClassResponseDTO findById(Long id) {
        GroupClass groupClass = groupClassRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));
        return GroupClassResponseDTO.fromEntity(groupClass);
    }

    public GroupClassResponseDTO update(Long id, GroupClassDTO groupClassDTO) {
        GroupClass existing = groupClassRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        // Verificar que el subjectId no sea nulo
        if (groupClassDTO.getSubjectId() == null) {
            throw new CustomException(ErrorCode.SUBJECT_NOT_FOUND);
        }

        // Verificar que exista la materia
        Subject subject = subjectRepository.findById(groupClassDTO.getSubjectId())
                .orElseThrow(() -> new CustomException(ErrorCode.SUBJECT_NOT_FOUND));

        // Verificar si ya existe otro grupo con el mismo nombre para esta materia (excepto este mismo grupo)
        if (!existing.getName().equals(groupClassDTO.getName()) &&
                groupClassRepository.existsByNameAndSubjectId(groupClassDTO.getName(), groupClassDTO.getSubjectId())) {
            throw new CustomException(ErrorCode.GROUP_ALREADY_EXISTS);
        }

        existing.setName(groupClassDTO.getName());
        existing.setCapacity(groupClassDTO.getCapacity());
        existing.setSubject(subject);

        GroupClass updatedGroupClass = groupClassRepository.save(existing);
        return GroupClassResponseDTO.fromEntity(updatedGroupClass);
    }

    public void delete(Long id) {
        if (!groupClassRepository.existsById(id)) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }
        groupClassRepository.deleteById(id);
    }
}