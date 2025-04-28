package co.edu.udes.backend.repositories;

import co.edu.udes.backend.dto.place.PlaceDTO;
import co.edu.udes.backend.models.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place,Long> {
    boolean existsByName(String name);
}
