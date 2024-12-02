package ngo.spine.eigenschuldapi.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ngo.spine.eigenschuldapi.DTO.ExerciseProgressDTO;
import ngo.spine.eigenschuldapi.Services.AesEncrypter;

import java.util.Date;
import java.util.UUID;

@Entity
public class ExerciseProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exercise_user_id")
    public UUID id;
    @Convert(converter = AesEncrypter.class)
    public Date firstOpen;
    @Convert(converter = AesEncrypter.class)
    public Date lastOpen;

    public UUID exerciseProgressId;

    private Boolean isDone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    public User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id")
    public ExerciseData exerciseData;


    public ExerciseProgress(User user, ExerciseData exerciseData) {
        this.user = user;
        this.exerciseData = exerciseData;

        this.firstOpen = null;
        this.lastOpen = null;
        this.isDone = false;
    }

    public ExerciseProgress() { }

    public void setFirstOpen(Date firstOpen) {
        this.firstOpen = firstOpen;
    }

    public Date getFirstOpen() {
        return firstOpen;
    }

    public void setLastOpen(Date lastOpen) {
        this.lastOpen = lastOpen;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    @JsonIgnore
    public ExerciseProgressDTO getDTO() {
        return new ExerciseProgressDTO(
            this.id,
            this.exerciseData.name,
            this.lastOpen,
            this.firstOpen,
            this.exerciseProgressId,
            this.isDone
        );
    }
}
