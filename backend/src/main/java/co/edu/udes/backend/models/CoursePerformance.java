package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "course_performances")
public class CoursePerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @OneToMany(mappedBy = "evaluation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Calification> califications;

    public void addCalification(Calification calification) {
        this.califications.add(calification);
    }

    public void removeCalification(Calification calification) {
        this.califications.remove(calification);
    }

    public Double calculateAverageScore() {
        return califications.stream()
                .mapToDouble(Calification::getScore)
                .average()
                .orElse(0.0);
    }

    public CoursePerformance(Student student, Group group, List<Calification> califications) {
        this.student = student;
        this.group = group;
        this.califications = califications;
    }

    public CoursePerformance() {
    }
}
