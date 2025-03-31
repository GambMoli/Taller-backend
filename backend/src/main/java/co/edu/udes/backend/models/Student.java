package co.edu.udes.backend.models;

import co.edu.udes.backend.models.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Student")
public class Student extends User {
    @Column (name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "enrollmentStatus")
    private StudentStatus enrollmentStatus;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL) //One student can get many enrollments.
    private List<Enrollment> enrollments;

    public void manageEnrollment() {
        System.out.println("Managing enrollment");
    }

    public Student(String code, StudentStatus enrollmentStatus, List<Enrollment> enrollments) {
        super();
        this.code = code;
        this.enrollmentStatus = enrollmentStatus;
        this.enrollments = enrollments;
    }
}
