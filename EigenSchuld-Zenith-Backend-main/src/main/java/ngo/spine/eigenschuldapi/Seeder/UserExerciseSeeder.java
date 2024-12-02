package ngo.spine.eigenschuldapi.Seeder;

import jakarta.transaction.Transactional;
import ngo.spine.eigenschuldapi.DAO.Chart.ChartDAO;
import ngo.spine.eigenschuldapi.DAO.Exercise.ExerciseDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseChart.ExerciseChartDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseUser.ExerciseUserDAO;
import ngo.spine.eigenschuldapi.DAO.User.UserDAO;
import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserExerciseSeeder {

    private final ExerciseDAO exerciseDAO;

    @Autowired
    public UserExerciseSeeder(ExerciseDAO exerciseDAO) {
        this.exerciseDAO = exerciseDAO;
    }

    public void seed() {
        ExerciseData exerciseData1 = new ExerciseData("Opdracht 1 - Grafieken");
        this.exerciseDAO.save(exerciseData1);

        ExerciseData exerciseData2 = new ExerciseData("Opdracht 2 - Vragen");
        this.exerciseDAO.save(exerciseData2);

        ExerciseData exerciseData3 = new ExerciseData("Opdracht 3 - Vragen");
        this.exerciseDAO.save(exerciseData3);

        ExerciseData exerciseData4 = new ExerciseData("Opdracht 4 - Grafieken");
        this.exerciseDAO.save(exerciseData4);

        ExerciseData exerciseData5 = new ExerciseData("Opdracht 5 - Brief");
        this.exerciseDAO.save(exerciseData5);
    }
}
