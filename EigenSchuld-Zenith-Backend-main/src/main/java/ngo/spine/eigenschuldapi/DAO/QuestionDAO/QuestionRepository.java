package ngo.spine.eigenschuldapi.DAO.QuestionDAO;

import ngo.spine.eigenschuldapi.Model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
