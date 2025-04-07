package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.GroupClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupClassRepository extends JpaRepository<GroupClass, Long> {
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GroupClass g WHERE g.name = :name AND g.subject.id = :subjectId")
    boolean existsByNameAndSubjectId(@Param("name") String name, @Param("subjectId") Long subjectId);
}
