package co.edu.udes.backend.repositories;

import co.edu.udes.backend.models.Forum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository  extends JpaRepository<Forum, Long> {
}
