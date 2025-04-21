package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;
    @Column(name = "Type" , nullable = false, unique = true)
    private String type;
    @Column(name = "Name" , nullable = false,unique = true)
    private String name;
    @Column(name="Quantity",nullable = false)
    private int quantity;
    @Column(name = "Available" , nullable = false)
    private boolean available;
    @Column(name = "Description" , nullable = false)
    private String description;
    @OneToMany(mappedBy = "place")
    private List<Reserve> reserve;
}
