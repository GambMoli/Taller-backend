package co.edu.udes.backend.models;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private GroupClass group;

    private String name;

    private String fileName;

    @Lob
    private byte[] data;
}

