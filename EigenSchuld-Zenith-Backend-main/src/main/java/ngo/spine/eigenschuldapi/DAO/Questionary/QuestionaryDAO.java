package ngo.spine.eigenschuldapi.DAO.Questionary;

import ngo.spine.eigenschuldapi.Model.Questionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
public class QuestionaryDAO {

    private final QuestionaryRepository questionaryRepository;

    @Autowired
    public QuestionaryDAO(QuestionaryRepository questionaryRepository) {
        this.questionaryRepository = questionaryRepository;
    }

    public void save(Questionary questionary) {
        this.questionaryRepository.save(questionary);
    }

    public Questionary getById(UUID id) throws ResponseStatusException {
        Optional<Questionary> optionalQuestionary = this.questionaryRepository.findById(id);
        return optionalQuestionary.orElseThrow(() -> new ResponseStatusException(HttpStatusCode.valueOf(404)));
    }

    public UUID getQuestionaryIdByQuestionId(UUID questionId) {
        return questionaryRepository.findQuestionaryIdByQuestionId(questionId);
    }
}
