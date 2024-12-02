package ngo.spine.eigenschuldapi.DAO.Exercise;

import ngo.spine.eigenschuldapi.Model.ExerciseData;
import org.springframework.stereotype.Component;

@Component
public class ExerciseDAO {

    private final ExerciseRepository exerciseRepository;

    public ExerciseDAO(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    public void save(ExerciseData exerciseData) {
        this.exerciseRepository.save(exerciseData);
    }

    public ExerciseData getExerciseByName(String name) {
        return exerciseRepository.getFirstExerciseWithName(name);
    }
}
