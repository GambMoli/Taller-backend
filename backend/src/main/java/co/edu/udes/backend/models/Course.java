package co.edu.udes.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "credits", nullable = false)
    private int credits;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @ManyToMany
    @JoinTable(
            name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private List<Course> preRequisitesCourses;

    @ManyToMany(mappedBy = "courses")
    private List<Student> studentsEnrolled;

    public void addStudent(Student student) {
        if (studentsEnrolled.size() < capacity) {
            studentsEnrolled.add(student);
        } else {
            throw new IllegalStateException("Course capacity reached");
        }
    }

    public void removeStudent(Student student) {
        studentsEnrolled.remove(student);
    }

    public Course(String title, String description, int credits, int capacity, List<Course> preRequisitesCourses, List<Student> studentsEnrolled) {
        this.title = title;
        this.description = description;
        this.credits = credits;
        this.capacity = capacity;
        this.preRequisitesCourses = preRequisitesCourses;
        this.studentsEnrolled = studentsEnrolled;
    }

    public Course() {}
}
