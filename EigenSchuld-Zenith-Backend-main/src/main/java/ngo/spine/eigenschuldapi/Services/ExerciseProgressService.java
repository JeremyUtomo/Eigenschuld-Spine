package ngo.spine.eigenschuldapi.Services;

import jakarta.transaction.Transactional;
import ngo.spine.eigenschuldapi.DAO.Chart.ChartDAO;
import ngo.spine.eigenschuldapi.DAO.Exercise.ExerciseDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseChart.ExerciseChartDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseUser.ExerciseUserDAO;
import ngo.spine.eigenschuldapi.DAO.Letter.LetterDAO;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.DAO.User.UserDAO;
import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.*;

@Component
public class ExerciseProgressService {

    private final UserDAO userDAO;
    private final ChartDAO chartDAO;
    private final ExerciseDAO exerciseDAO;
    private final ExerciseUserDAO exerciseUserDAO;
    private final ExerciseChartDAO exerciseChartDAO;
    private final QuestionaryDAO questionaryDAO;
    private final LetterDAO letterDAO;

    @Autowired
    public ExerciseProgressService(
        UserDAO userDAO,
        ExerciseDAO exerciseDAO,
        ExerciseUserDAO exerciseUserDAO,
        ExerciseChartDAO exerciseChartDAO,
        ChartDAO chartDAO,
        QuestionaryDAO questionaryDAO,
        LetterDAO letterDAO
    ) {
        this.userDAO = userDAO;
        this.chartDAO = chartDAO;
        this.exerciseDAO = exerciseDAO;
        this.exerciseUserDAO = exerciseUserDAO;
        this.exerciseChartDAO = exerciseChartDAO;
        this.questionaryDAO = questionaryDAO;
        this.letterDAO = letterDAO;
    }

    public void addExercisesToClient(String userId) {
        Optional<User> optionalUser = userDAO.getById(userId);

        if (!optionalUser.isPresent()) {
            return;
        }

        User user = optionalUser.get();

        addChartExercise(user, "Opdracht 1 - Grafieken");
        addQuestionExercise(user, "Opdracht 2 - Vragen");
        addQuestionExercise(user, "Opdracht 3 - Vragen");
        addChartExercise(user, "Opdracht 4 - Grafieken");
        addLetterExercise(user, "Opdracht 5 - Brief");
    }

    @Transactional
    public void addChartExercise(User user, String exerciseName) {
        ExerciseData exerciseData = this.exerciseDAO.getExerciseByName(exerciseName);
        ExerciseProgress newExercise = new ExerciseProgress(user, exerciseData);

        ChartOverview chart = new ChartOverview(user);

        Chart feel = new Chart(user);
        Chart sense = new Chart(user);

        this.chartDAO.save(feel);
        this.chartDAO.save(sense);

        chart.setChartSense(sense);
        chart.setChartFeeling(feel);

        this.exerciseChartDAO.save(chart);

        newExercise.exerciseProgressId = chart.id;

        user.addExercise(newExercise);

        this.exerciseUserDAO.save(newExercise);
        this.userDAO.save(user);

        this.chartDAO.save(feel);
        this.chartDAO.save(sense);
    }

    @Transactional
    public void addQuestionExercise(User user, String exerciseName) {
        ExerciseData exerciseData = this.exerciseDAO.getExerciseByName(exerciseName);
        ExerciseProgress newExercise = new ExerciseProgress(user, exerciseData);

        Questionary questionary = new Questionary(user);

        this.questionaryDAO.save(questionary);
        newExercise.exerciseProgressId = questionary.id;

        user.addExercise(newExercise);

        this.exerciseUserDAO.save(newExercise);
        this.userDAO.save(user);
    }

    @Transactional
    public void addLetterExercise(User user, String exerciseName) {
        ExerciseData exerciseData = exerciseDAO.getExerciseByName(exerciseName);
        ExerciseProgress newExercise = new ExerciseProgress(user, exerciseData);

        Letter newLetter = new Letter(user);

        this.letterDAO.saveLetter(newLetter);
        newExercise.exerciseProgressId = newLetter.getId();

        user.addExercise(newExercise);

        this.exerciseUserDAO.save(newExercise);
        this.userDAO.save(user);
    }

    public List<UserDTO> getClientsProgressByHulpverlenerId(UUID userId) {
        User hulpverlener = userDAO.getHulpverlenerWithClients(userId);
        return hulpverlener.getClients().stream()
            .map(User::getDTO)
            .collect(Collectors.toList());
    }

    public ArrayList<ExerciseProgressDTO> getClientProgressByClientId(UUID userId) {
        Optional<User> user = userDAO.getById(userId);

        return user.get().getDTO().exerciseProgressIds;
    }

    public void setDateForExerciseProgress(String id){
        ExerciseProgress exerciseProgress = this.exerciseUserDAO.getByExerciseProgressId(UUID.fromString(id));

        if(exerciseProgress.getFirstOpen()==null){
            exerciseProgress.setFirstOpen(new Date());
        }

        exerciseProgress.setLastOpen(new Date());
        this.exerciseUserDAO.save(exerciseProgress);
    }

    public void setIsDoneForExerciseProgress(UUID id){
        ExerciseProgress exerciseProgress = this.exerciseUserDAO.getByExerciseProgressId(id);
        exerciseProgress.setDone(true);
        this.exerciseUserDAO.save(exerciseProgress);
    }
}
