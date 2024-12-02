package ngo.spine.eigenschuldapi.Model;

import jakarta.persistence.*;

import java.time.*;
import java.util.*;

@Entity
public class InvitationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String inviteToken;
    @ManyToOne
    @JoinColumn(name = "hulpverler_id")
    private User hulpverlener;
    private LocalDateTime expiryDate;

    public InvitationToken() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getInviteToken() {
        return inviteToken;
    }

    public void setInviteToken(String token) {
        this.inviteToken = token;
    }

    public User getHulpverlener() {
        return hulpverlener;
    }

    public void setHulpverlener(User hulpverlenerId) {
        this.hulpverlener = hulpverlenerId;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expireDate) {
        this.expiryDate = expireDate;
    }
}
