package ngo.spine.eigenschuldapi.DTO;

import java.util.UUID;

public class AuthenticationDTO {

    private String token;
    private UUID userId;

    public AuthenticationDTO(String token, UUID userId) {
        this.token = token;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public UUID getUserId() {
        return userId;
    }
}
