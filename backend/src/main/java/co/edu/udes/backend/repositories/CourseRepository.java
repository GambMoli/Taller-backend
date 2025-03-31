package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository <Course, Long> {
}
