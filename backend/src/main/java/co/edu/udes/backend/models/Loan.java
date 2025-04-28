package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @JoinColumn(name = "teacher_id",nullable = true)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "student_id",nullable = true)
    private Student student;

    @Column(name = "loan_date", nullable = true)
    private LocalDateTime   loanDate;

    @Column(name = "deadline", nullable = true)
    private LocalDateTime   deadline;

    @Column(name = "actual_return_date" , nullable = true)
    private LocalDateTime   actualReturnDate;

    @Column(name = "return_state")
    private String returnState;

    @Column(name = "status", nullable = false)
    private String status ;
}
