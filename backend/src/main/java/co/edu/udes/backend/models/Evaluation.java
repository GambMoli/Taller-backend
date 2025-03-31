package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name= "totalPoints")
    private Double totalPoints;

    @Column(name = "description")
    private String description;

    @Column (name= "feedback")
    private String feedback;

    @ManyToOne
    @JoinColumn(name = "group")
    private Group group;

    public Evaluation(Long id, String title, String description, Group group) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.group = group;
    }
}