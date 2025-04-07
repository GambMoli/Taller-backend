package co.edu.udes.backend.models.enums;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;

@Entity
@Table(name= "students")
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column (nullable = false)
    private String code;

//    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
//    private Set<Enrollment> enrollments = new HashSet<>();

    //    @OneToMany(mappedBy = "students", cascade = CascadeType.ALL)
//    private Set<Enrollment> enrollments = new HashSet<>();



}
