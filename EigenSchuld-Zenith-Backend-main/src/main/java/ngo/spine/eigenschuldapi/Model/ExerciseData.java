package ngo.spine.eigenschuldapi.Model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ExerciseData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exercise_id")
    public UUID id;

    public String name;

    public ExerciseData(String name) {
        this.name = name;
    }

    public ExerciseData() { }
}
