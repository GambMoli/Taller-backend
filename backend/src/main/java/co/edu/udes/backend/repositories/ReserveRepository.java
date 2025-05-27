package co.edu.udes.backend.repositories;


import co.edu.udes.backend.models.Place;
import co.edu.udes.backend.models.Reserve;

import co.edu.udes.backend.models.Student;
import co.edu.udes.backend.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.Date;
import java.util.List;
@Repository
public interface ReserveRepository extends JpaRepository<Reserve,Long> {
    boolean existsByCode(String code);
    boolean existsByPlaceAndHourInitAndReserveDate(Place place, LocalTime hourInit, LocalDate reserveDate);
    List<Reserve> findByStudent(Student studentId);
    List<Reserve> findByTeacher(Teacher teacher);
    boolean existsByPlaceAndHourInitAndReserveDateAndIdNot(Place place, LocalTime hourInit, LocalDate reserveDate, Long id);

}
