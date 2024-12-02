package ngo.spine.eigenschuldapi.DAO.ExerciseUser;

import ngo.spine.eigenschuldapi.Model.ExerciseProgress;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExerciseUserDAO {

    private final ExerciseUserRepository repository;

    public ExerciseUserDAO(ExerciseUserRepository repository) {
        this.repository = repository;
    }

    public ExerciseProgress getByExerciseProgressId(UUID id){
        return this.repository.findByExerciseProgressId(id);
    }

    public void save(ExerciseProgress exerciseProgress) {
        this.repository.save(exerciseProgress);
    }
}
