package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Code",nullable = false)
    private String code;

    @Column(name = "Description", nullable = false)
    private String description;

    @Column(name = "Type", nullable = false)
    private String type;

    @Column(name = "State", nullable = false)
    private String state;

    @Column(name = "Stock", nullable = false)
    private int stock;

    @Column(name = "EntryDate", nullable = true)
    private LocalDate entryDate;

    @OneToMany(mappedBy = "material")
    private List<Loan> loan;

    @OneToMany(mappedBy = "material")
    private List<MaintenanceRecord> maintenanceRecords;
}