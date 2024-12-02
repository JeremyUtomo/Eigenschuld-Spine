package ngo.spine.eigenschuldapi.DAO.InvitationToken;

import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class InvitationTokenDAO {
    private final InvitationTokenRepository invitationTokenRepository;

    @Autowired
    public InvitationTokenDAO(InvitationTokenRepository invitationTokenRepository) {
        this.invitationTokenRepository = invitationTokenRepository;
    }

    public Optional<InvitationToken> findByInviteToken (String inviteToken){
        return this.invitationTokenRepository.findByInviteToken(inviteToken);
    }

    public void save(InvitationToken invitationToken) {
        this.invitationTokenRepository.save(invitationToken);
    }


    public void deleteById(UUID id) {
        this.invitationTokenRepository.deleteById(id);
    }
}
