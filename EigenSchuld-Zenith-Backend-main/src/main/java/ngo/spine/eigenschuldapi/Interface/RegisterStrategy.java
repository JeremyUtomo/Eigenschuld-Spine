package ngo.spine.eigenschuldapi.Interface;

import ngo.spine.eigenschuldapi.DTO.AuthenticationDTO;
import ngo.spine.eigenschuldapi.Model.Role;
import ngo.spine.eigenschuldapi.Model.User;
import org.springframework.stereotype.Component;

@Component
public interface RegisterStrategy {
    AuthenticationDTO register(User request, Role role, String token);
}
