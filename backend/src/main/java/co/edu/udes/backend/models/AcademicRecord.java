package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "academic_records")
public class AcademicRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToMany(mappedBy = "academicRecord", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments;

    @ElementCollection
    @CollectionTable(name = "completed_courses", joinColumns = @JoinColumn(name = "academic_record_id"))
    @MapKeyJoinColumn(name = "course_id")
    @Column(name = "grade")
    private Map<Course, Double> completedCourses;

    @ManyToMany
    @JoinTable(
            name = "current_courses",
            joinColumns = @JoinColumn(name = "academic_record_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> currentCourses;

    @Column(name = "total_credits_approved")
    private Integer totalCreditsApproved;

    @Column(name = "gpa")
    private Double GPA;

    @Column(name = "academic_status")
    private String academicStatus;

    @OneToMany(mappedBy = "academicRecord")
    private List<CoursePerformance> coursePerformances;

    public AcademicRecord(Student student) {
    }

    public void addCompletedCourse(Course course, Double grade) {
        this.completedCourses.put(course, grade);
    }

    public Integer getTotalCredits() {
        return completedCourses.size();
    }

    public AcademicRecord(Long id, Student student, List<Enrollment> enrollments, Map<Course, Double> completedCourses, List<Course> currentCourses, Integer totalCreditsApproved, Double GPA, String academicStatus, List<CoursePerformance> coursePerformances) {
        this.id = id;
        this.student = student;
        this.enrollments = enrollments;
        this.completedCourses = completedCourses;
        this.currentCourses = currentCourses;
        this.totalCreditsApproved = totalCreditsApproved;
        this.GPA = GPA;
        this.academicStatus = academicStatus;
        this.coursePerformances = coursePerformances;
    }

    public Double calculateGPA() {

        return GPA;
    }

}
