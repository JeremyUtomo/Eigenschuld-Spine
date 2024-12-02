package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.InvitationToken.*;
import ngo.spine.eigenschuldapi.DTO.AuthenticationDTO;
import ngo.spine.eigenschuldapi.Interface.*;
import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.DAO.User.UserRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.*;

import java.time.*;
import java.util.*;

@Component("clientRegistrationService")
@Primary
public class ClientRegistrationService implements RegisterStrategy {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ExerciseProgressService exerciseProgressService;
    private final InvitationTokenDAO invitationTokenDAO;
    private final UserService userService;
    private final RegisterErrorService registerErrorService;

    @Autowired
    public ClientRegistrationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        ExerciseProgressService exerciseProgressService,
        InvitationTokenDAO invitationTokenDAO,
        UserService userService,
        RegisterErrorService registerErrorService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.exerciseProgressService = exerciseProgressService;
        this.invitationTokenDAO = invitationTokenDAO;
        this.userService = userService;
        this.registerErrorService = registerErrorService;
    }

    @Override
    public AuthenticationDTO register (User request, Role role, String inviteToken) {
        Optional<User> existingUser = userRepository.findUserByEmail(request.getEmail());
        Optional<InvitationToken> invitationTokenOptional = invitationTokenDAO.findByInviteToken(inviteToken);

        this.validateInvitationTokenExists(invitationTokenOptional);
        this.validateInvitationTokenNotExpired(invitationTokenOptional);
        this.registerErrorService.validateUserDoesNotExist(existingUser);
        this.registerErrorService.validatePassword(request.getPassword());

        InvitationToken invitationToken = invitationTokenOptional.get();

        User user = userService.createUser(request, role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setHulpverlener(invitationToken.getHulpverlener());

        user = this.userRepository.save(user);
        String jwtToken = this.jwtService.generateToken(user);

        this.exerciseProgressService.addExercisesToClient(user.getId().toString());
        this.invitationTokenDAO.deleteById(invitationToken.getId());

        return new AuthenticationDTO(jwtToken, user.getId());
    }

    private void validateInvitationTokenExists(Optional<InvitationToken> invitationTokenOptional) {
        if (invitationTokenOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite token not found");
        }
    }

    private void validateInvitationTokenNotExpired(Optional<InvitationToken> invitationTokenOptional) {
        if (invitationTokenOptional.get().getExpiryDate().isBefore(LocalDateTime.now())) {
            invitationTokenDAO.deleteById(invitationTokenOptional.get().getId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Registration Link expired");
        }
    }
}
