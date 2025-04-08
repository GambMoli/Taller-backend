package co.edu.udes.backend.service;

import co.edu.udes.backend.dto.schedule.ScheduleDTO;
import co.edu.udes.backend.dto.schedule.ScheduleResponseDTO;
import co.edu.udes.backend.enums.ErrorCode;
import co.edu.udes.backend.exceptions.CustomException;
import co.edu.udes.backend.models.GroupClass;
import co.edu.udes.backend.models.Schedule;
import co.edu.udes.backend.repositories.GroupClassRepository;
import co.edu.udes.backend.repositories.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final GroupClassRepository groupClassRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, GroupClassRepository groupClassRepository) {
        this.scheduleRepository = scheduleRepository;
        this.groupClassRepository = groupClassRepository;
    }

    private boolean isOverlapping(String start1, String end1, String start2, String end2) {
        int s1 = toMinutes(start1);
        int e1 = toMinutes(end1);
        int s2 = toMinutes(start2);
        int e2 = toMinutes(end2);

        return s1 < e2 && s2 < e1;
    }

    private int toMinutes(String time) {
        String[] parts = time.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public ScheduleResponseDTO create(ScheduleDTO scheduleDTO) {
        GroupClass group = groupClassRepository.findById(scheduleDTO.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        Set<Schedule> existingSchedules = scheduleRepository.findByGroupId(group.getId());

        for (Schedule existing : existingSchedules) {
            if (existing.getDay().equals(scheduleDTO.getDay()) &&
                    isOverlapping(scheduleDTO.getStartTime(), scheduleDTO.getEndTime(),
                            existing.getStartTime(), existing.getEndTime())) {
                throw new CustomException(ErrorCode.GROUP_SCHEDULE_CONFLICT);
            }
        }

        Schedule schedule = new Schedule();
        schedule.setDay(scheduleDTO.getDay());
        schedule.setStartTime(scheduleDTO.getStartTime());
        schedule.setEndTime(scheduleDTO.getEndTime());
        schedule.setClassroom(scheduleDTO.getClassroom());
        schedule.setGroup(group);

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return ScheduleResponseDTO.fromEntity(savedSchedule);
    }

    public List<ScheduleResponseDTO> findAll() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ScheduleResponseDTO findById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
        return ScheduleResponseDTO.fromEntity(schedule);
    }

    public Set<ScheduleResponseDTO> findByGroupId(Long groupId) {
        if (!groupClassRepository.existsById(groupId)) {
            throw new CustomException(ErrorCode.GROUP_NOT_FOUND);
        }

        return scheduleRepository.findByGroupId(groupId).stream()
                .map(ScheduleResponseDTO::fromEntity)
                .collect(Collectors.toSet());
    }

    public ScheduleResponseDTO update(Long id, ScheduleDTO scheduleDTO) {
        Schedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));

        GroupClass group = groupClassRepository.findById(scheduleDTO.getGroupId())
                .orElseThrow(() -> new CustomException(ErrorCode.GROUP_NOT_FOUND));

        existing.setDay(scheduleDTO.getDay());
        existing.setStartTime(scheduleDTO.getStartTime());
        existing.setEndTime(scheduleDTO.getEndTime());
        existing.setClassroom(scheduleDTO.getClassroom());
        existing.setGroup(group);

        Schedule updatedSchedule = scheduleRepository.save(existing);
        return ScheduleResponseDTO.fromEntity(updatedSchedule);
    }

    public void delete(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new CustomException(ErrorCode.SCHEDULE_NOT_FOUND);
        }
        scheduleRepository.deleteById(id);
    }
}
