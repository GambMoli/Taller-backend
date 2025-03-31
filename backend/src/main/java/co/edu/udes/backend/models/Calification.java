package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "califications")
public class Calification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "score")
    private Double score;

    @Getter
    @Column(name = "comments")
    private String comments;

    @Column(name = "submissionDate")
    private Date submissionDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;

    public void assignCalification(Double score) {
        this.score = score;
    }

    public void updateCalification(Double score) {
        this.score = score;
    }

    public void addComment(String comment) {
        this.comments = comment;
    }

}
