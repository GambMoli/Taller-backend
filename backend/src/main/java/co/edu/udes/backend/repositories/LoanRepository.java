package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    boolean existsByCode(String code);
}
