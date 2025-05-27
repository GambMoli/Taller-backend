package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Loan;
import co.edu.udes.backend.models.Reserve;
import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    boolean existsByCode(String code);

    List<Loan> findByStudent(Student studentId);
    List<Loan> findByTeacher(Teacher teacher);
}
