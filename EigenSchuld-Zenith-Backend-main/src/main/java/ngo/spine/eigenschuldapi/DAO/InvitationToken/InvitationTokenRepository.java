package ngo.spine.eigenschuldapi.DAO.InvitationToken;

import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface InvitationTokenRepository extends JpaRepository<InvitationToken, UUID> {
    Optional<InvitationToken> findByInviteToken(String InviteToken);
}
