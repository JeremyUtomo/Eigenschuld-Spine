package ngo.spine.eigenschuldapi.DAO.Exercise;

import ngo.spine.eigenschuldapi.Model.ExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ExerciseRepository extends JpaRepository<ExerciseData, UUID> {
    @Query(value = "SELECT * FROM exercise_data WHERE exercise_data.name = :#{#exerciseTitle} LIMIT 1",
        nativeQuery = true)
    ExerciseData getFirstExerciseWithName(@Param("exerciseTitle") String exerciseTitle);
}
