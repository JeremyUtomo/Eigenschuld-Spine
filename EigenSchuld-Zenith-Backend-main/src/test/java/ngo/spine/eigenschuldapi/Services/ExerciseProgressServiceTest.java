package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Chart.ChartDAO;
import ngo.spine.eigenschuldapi.DAO.Exercise.ExerciseDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseChart.ExerciseChartDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseUser.ExerciseUserDAO;
import ngo.spine.eigenschuldapi.DAO.Letter.LetterDAO;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DAO.User.UserDAO;
import ngo.spine.eigenschuldapi.Model.ExerciseData;
import ngo.spine.eigenschuldapi.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExerciseProgressServiceTest {

    @Mock UserDAO userDAO;
    @Mock ChartDAO chartDAO;
    @Mock ExerciseDAO exerciseDAO;
    @Mock ExerciseUserDAO exerciseUserDAO;
    @Mock ExerciseChartDAO exerciseChartDAO;
    @Mock QuestionaryDAO questionaryDAO;
    @Mock LetterDAO letterDAO;


    private ExerciseProgressService SUT = null;

    @BeforeEach
    public void init() {
        this.SUT = new ExerciseProgressService(userDAO, exerciseDAO, exerciseUserDAO, exerciseChartDAO, chartDAO, questionaryDAO, letterDAO);
    }

    @Test
    void should_add_exercise_to_user() {
        User user = new User();
        String exerciseName = "Opdracht 1 - Grafieken";
        int expectedResult = 1;

        when(exerciseDAO.getExerciseByName(any(String.class)))
            .thenReturn(new ExerciseData(exerciseName));

        SUT.addChartExercise(user, exerciseName);

        assertEquals(expectedResult, user.fetchExerciseIds().size());
    }
}
