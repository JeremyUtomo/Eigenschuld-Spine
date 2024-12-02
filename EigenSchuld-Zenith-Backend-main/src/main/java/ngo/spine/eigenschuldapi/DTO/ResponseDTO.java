package ngo.spine.eigenschuldapi.DTO;

import ngo.spine.eigenschuldapi.Model.User;
import java.time.LocalDateTime;
import java.util.UUID;

public class ResponseDTO {
    public UUID response_id;
    public String response;
    public UUID author_id;
    public String firstName;
    public String lastName;
    public LocalDateTime responseTime;

    public ResponseDTO(UUID response_id, String response, User user, LocalDateTime responseTime) {
        this.response_id = response_id;
        this.response = response;
        this.author_id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.responseTime = responseTime;
    }

    public ResponseDTO(UUID response_id, String response, UUID author_id, String firstName, String lastName, LocalDateTime responseTime) {
        this.response_id = response_id;
        this.response = response;
        this.author_id = author_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.responseTime = responseTime;
    }

    public ResponseDTO() {
    }
}
