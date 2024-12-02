package ngo.spine.eigenschuldapi.DTO;

import java.util.Date;
import java.util.UUID;

public class ExerciseProgressDTO {
    public UUID id;
    public String name;
    public Date lastOpen;
    public Date firstOpen;
    public UUID progressId;
    public Boolean isDone;

    public ExerciseProgressDTO(UUID id, String name, Date lastOpen, Date firstOpen, UUID progressId, Boolean isDone) {
        this.id = id;
        this.name = name;
        this.lastOpen = lastOpen;
        this.firstOpen = firstOpen;
        this.progressId = progressId;
        this.isDone = isDone;
    }
}
