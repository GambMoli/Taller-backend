package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.attendance.*;
import co.edu.udes.backend.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark")
    public ResponseEntity<AttendanceResponseDTO> markAttendance(@RequestBody AttendanceCreateDTO dto) {
        AttendanceResponseDTO response = attendanceService.markAttendance(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/mark-bulk")
    public ResponseEntity<List<AttendanceResponseDTO>> markBulkAttendance(@RequestBody BulkAttendanceDTO dto) {
        List<AttendanceResponseDTO> response = attendanceService.markBulkAttendance(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/class-dates")
    public ResponseEntity<ClassDatesDTO> getClassDates(
            @RequestParam Long groupId,
            @RequestParam Long periodId) {
        ClassDatesDTO response = attendanceService.getClassDatesForGroupAndPeriod(groupId, periodId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student-record")
    public ResponseEntity<StudentAttendanceRecordDTO> getStudentAttendanceRecord(
            @RequestParam Long studentId,
            @RequestParam Long groupId,
            @RequestParam Long periodId) {
        StudentAttendanceRecordDTO response = attendanceService.getStudentAttendanceRecord(studentId, groupId, periodId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/group-records")
    public ResponseEntity<List<StudentAttendanceRecordDTO>> getGroupAttendanceRecords(
            @RequestParam Long groupId,
            @RequestParam Long periodId) {
        List<StudentAttendanceRecordDTO> response = attendanceService.getGroupAttendanceRecords(groupId, periodId);
        return ResponseEntity.ok(response);
    }
}