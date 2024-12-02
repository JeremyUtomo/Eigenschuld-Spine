package ngo.spine.eigenschuldapi.DAO.Questionary;

import ngo.spine.eigenschuldapi.Model.Questionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface QuestionaryRepository extends JpaRepository<Questionary, UUID> {
    @Query("SELECT q.id FROM Questionary q JOIN q.questions qu WHERE qu.id = :questionId")
    UUID findQuestionaryIdByQuestionId(UUID questionId);
}
