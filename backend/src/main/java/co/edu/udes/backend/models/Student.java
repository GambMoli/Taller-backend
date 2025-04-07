package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;
    private String documentNumber;

    @ManyToOne
    private Career career;  // Relación con Career

    @OneToMany(mappedBy = "student")
    private List<AcademicRecord> academicRecords;

    @OneToMany(mappedBy = "student")
    private List<Attendance> attendances;
}
