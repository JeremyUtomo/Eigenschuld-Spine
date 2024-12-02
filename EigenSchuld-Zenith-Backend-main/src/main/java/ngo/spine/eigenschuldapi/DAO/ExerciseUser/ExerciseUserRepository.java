package ngo.spine.eigenschuldapi.DAO.ExerciseUser;

import jakarta.persistence.criteria.From;
import ngo.spine.eigenschuldapi.Model.ExerciseProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ExerciseUserRepository extends JpaRepository<ExerciseProgress, UUID> {
    @Query ("SELECT e FROM ExerciseProgress e WHERE e.exerciseProgressId= :uuid ")
    ExerciseProgress findByExerciseProgressId(UUID uuid);
}
