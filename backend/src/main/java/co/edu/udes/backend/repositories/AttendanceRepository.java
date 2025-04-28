package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByGroupIdAndClassDate(Long groupId, LocalDate classDate);

    List<Attendance> findByStudentIdAndGroupId(Long studentId, Long groupId);

    List<Attendance> findByGroupIdAndPeriodId(Long groupId, Long periodId);

    List<Attendance> findByStudentIdAndGroupIdAndPeriodId(Long studentId, Long groupId, Long periodId);

    Optional<Attendance> findByStudentIdAndGroupIdAndClassDate(Long studentId, Long groupId, LocalDate classDate);

    boolean existsByStudentIdAndGroupIdAndClassDate(Long studentId, Long groupId, LocalDate classDate);

    @Query("SELECT a.classDate FROM Attendance a WHERE a.group.id = :groupId AND a.period.id = :periodId GROUP BY a.classDate")
    List<LocalDate> findDistinctClassDatesByGroupIdAndPeriodId(Long groupId, Long periodId);
}