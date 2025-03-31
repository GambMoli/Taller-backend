package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Setter
@Getter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name= "password")
    private String password;

    @Column(name= "email")
    private String email;

    @Column(name= "dateOfBirth")
    private Date dateOfBirth;

    public User(long id, String name, String password, String email, Date dateOfBirth) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public User() {

    }
}
