package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne()
    @JoinColumn(name = "career_id")
    private Career career;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_groups",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<GroupClass> enrolledGroups = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Period> periods = new ArrayList<>();

    public void initializeDefaultPeriods() {
        if (this.career == null) {
            return;
        }

        for (Semester semester : this.career.getSemesters()) {

            Period period1 = new Period();
            period1.setName("Corte 1");
            period1.setStudent(this);
            period1.setSemester(semester);
            period1.setWeight(0.3); // 30%
            periods.add(period1);

            Period period2 = new Period();
            period2.setName("Corte 2");
            period2.setStudent(this);
            period2.setSemester(semester);
            period2.setWeight(0.3); // 30%
            periods.add(period2);

            Period period3 = new Period();
            period3.setName("Corte 3");
            period3.setStudent(this);
            period3.setSemester(semester);
            period3.setWeight(0.4); // 40%
            periods.add(period3);
        }
    }

}