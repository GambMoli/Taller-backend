package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Professor")
public class Professor extends User {

    @Column(name = "department")
    private String department;

    @Column(name = "workload")
    private int workload;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    private List<Group> assignedGroups;

    public Professor(String department, int workload, List<Group> assignedGroups) {
        super();
        this.department = department;
        this.workload = workload;
        this.assignedGroups = assignedGroups;
    }
}
