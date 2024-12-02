package ngo.spine.eigenschuldapi.DAO.Letter;

import ngo.spine.eigenschuldapi.Model.Letter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LetterRepository extends JpaRepository<Letter, UUID> {
}
