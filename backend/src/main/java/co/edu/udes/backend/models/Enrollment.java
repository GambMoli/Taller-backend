package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column (name = "enrollmentDate")
    private Date enrollmentDate;

    @Column (name = "period")
    private String period;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "academic_record_id")
    private AcademicRecord academicRecord;


    public Enrollment() {

    }

    public void enrollStudent(Student student) {
        // Implementation
    }

    public void checkPrerequisites() {
        // Implementation
    }

    public Enrollment(long id, Student student, Date enrollmentDate, String period, String status) {
        this.id = id;
        this.student = student;
        this.enrollmentDate = enrollmentDate;
        this.period = period;
        this.status = status;
    }
}
