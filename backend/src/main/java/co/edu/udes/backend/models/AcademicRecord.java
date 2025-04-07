package co.edu.udes.backend.models;

import co.edu.udes.backend.enums.AcademicStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class AcademicRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private GroupClass group;

    private Double grade;

    @Enumerated(EnumType.STRING)
    private AcademicStatus status;

    @OneToMany(mappedBy = "academicRecord")
    private List<Attendance> attendances;
}
