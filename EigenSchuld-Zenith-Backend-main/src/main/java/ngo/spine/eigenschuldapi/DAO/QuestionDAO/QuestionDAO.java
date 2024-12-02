package ngo.spine.eigenschuldapi.DAO.QuestionDAO;

import ngo.spine.eigenschuldapi.Model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class QuestionDAO {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionDAO(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Optional<Question> getQuestionById(UUID id) {
        return this.questionRepository.findById(id);
    }
}
