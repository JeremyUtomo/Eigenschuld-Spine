package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.QuestionDAO.QuestionDAO;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DTO.QuestionDTO;
import ngo.spine.eigenschuldapi.DTO.QuestionaryDTO;
import ngo.spine.eigenschuldapi.Model.Question;
import ngo.spine.eigenschuldapi.Model.Questionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QuestionaryServiceTest {

    private QuestionaryService SUT;
    private Questionary questionary;

    @Mock
    private QuestionaryDAO questionaryDAO;
    @Mock
    private QuestionDAO questionDAO;

    @BeforeEach
    void setUp() {
        this.SUT = new QuestionaryService(questionaryDAO, questionDAO);

        this.questionary = new Questionary();
        this.questionary.questions = new HashSet<>();

        this.questionary.questions.add(new Question("Hallo", 3));
        this.questionary.questions.add(new Question("Hallo", 2));
        this.questionary.questions.add(new Question("Hallo", 4));
        this.questionary.questions.add(new Question("Hallo", 1));
    }

    @Test
    void should_return_ordered_questionary() {
        Set<Question> orderedSet = new TreeSet<>(Comparator.naturalOrder());
        orderedSet.addAll(this.questionary.questions);
        ArrayList<QuestionDTO> expected = new ArrayList();

        for (Question question : orderedSet) {
            expected.add(new QuestionDTO(question.id, question.question, question.orderNumber));
        }

        when(questionaryDAO.getById(any(UUID.class))).thenReturn(this.questionary);

        QuestionaryDTO questionaryDTO = this.SUT.getQuestionaryById(UUID.randomUUID());

        assertEquals(expected.get(0).id, questionaryDTO.questions.get(0).id);
    }
}
