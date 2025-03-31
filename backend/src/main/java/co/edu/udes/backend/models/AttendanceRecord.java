package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "present")
    private boolean present;

    @Column(name = "status")
    private String status;

    @Column(name = "comments")
    private String comments;

    public AttendanceRecord() {
    }

    public AttendanceRecord(Student student, Course course, LocalDate date, boolean present, String status, String comments) {
        this.student = student;
        this.course = course;
        this.date = date;
        this.present = present;
        this.status = status;
        this.comments = comments;
    }
}