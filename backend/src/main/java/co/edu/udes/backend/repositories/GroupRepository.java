package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Course;
import co.edu.udes.backend.models.Group;
import co.edu.udes.backend.models.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCourse(Course course);

    List<Group> findByProfessor(Professor professor);
}
