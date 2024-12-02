package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.QuestionDAO.QuestionDAO;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DTO.QuestionDTO;
import ngo.spine.eigenschuldapi.DTO.QuestionaryDTO;
import ngo.spine.eigenschuldapi.DTO.ResponseDTO;
import ngo.spine.eigenschuldapi.Model.Question;
import ngo.spine.eigenschuldapi.Model.Questionary;
import ngo.spine.eigenschuldapi.Model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuestionaryService {

    private final QuestionaryDAO questionaryDAO;
    private final QuestionDAO questionDAO;

    @Autowired
    public QuestionaryService(QuestionaryDAO questionaryDAO, QuestionDAO questionDAO) {
        this.questionaryDAO = questionaryDAO;
        this.questionDAO = questionDAO;
    }

    public QuestionaryDTO getQuestionaryById(UUID id) {
        QuestionaryDTO questionaryDTO = new QuestionaryDTO();

        Questionary questionary = this.questionaryDAO.getById(id);
        Set<Question> oldQuestions = questionary.questions;

        questionary.questions = new TreeSet<>(Comparator.naturalOrder());
        questionary.questions.addAll(oldQuestions);

        questionaryDTO.questions = new ArrayList<>();

        for (Question question : questionary.questions) {
            QuestionDTO questionDTO = new QuestionDTO(question.id, question.question, question.orderNumber);

            List<Response> sortedResponses = new ArrayList<>(question.responses);
            sortedResponses.sort(Comparator.comparing(Response::getResponseTime));

            for (Response response : sortedResponses) {
                ResponseDTO responseDTO = new ResponseDTO(response.id, response.response, response.user, response.getResponseTime());
                questionDTO.responses.add(responseDTO);
            }

            questionaryDTO.questions.add(questionDTO);
        }

        return questionaryDTO;
    }

    public void saveNewQuestionary(UUID id, QuestionaryDTO requestBody) {
        Questionary questionary = this.questionaryDAO.getById(id);
        questionary.questions = new TreeSet<>(Comparator.naturalOrder());

        for (QuestionDTO questionDTO : requestBody.questions) {
            Optional<Question> question = this.questionDAO.getQuestionById(questionDTO.id);

            if (question.isEmpty()) {
                questionary.questions.add(new Question(questionDTO.question, questionDTO.orderNumber));
            } else {
                questionary.questions.add(question.get());
            }
        }

        this.questionaryDAO.save(questionary);
    }
}
