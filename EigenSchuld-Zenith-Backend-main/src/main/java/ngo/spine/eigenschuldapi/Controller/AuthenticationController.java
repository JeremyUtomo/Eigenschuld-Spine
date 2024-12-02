package ngo.spine.eigenschuldapi.Controller;


import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Interface.*;
import ngo.spine.eigenschuldapi.Services.*;
import ngo.spine.eigenschuldapi.Exception.*;
import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private AuthenticationService authservice;
    private final RegisterStrategy hulpverlenerRegistration;
    private final RegisterStrategy clientRegistration;

    public AuthenticationController(
        AuthenticationService authservice,
        @Qualifier("hulpverlenerRegistrationService") RegisterStrategy hulpverlenerRegistration,
        @Qualifier("clientRegistrationService") RegisterStrategy clientRegistration
    ) {
        this.authservice = authservice;
        this.hulpverlenerRegistration = hulpverlenerRegistration;
        this.clientRegistration = clientRegistration;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDTO> register(
            @RequestBody RegisterDTO request
    ) {
        User user = convertToUser(request);
        String inviteToken = request.getInviteToken();
        AuthenticationService context = new AuthenticationService(clientRegistration);
        return ResponseEntity.ok(context.executeStrategy(user, Role.CLIENT, inviteToken));
    }

    @PostMapping("/register/hulpverlener")
    public ResponseEntity<AuthenticationDTO> hulpverlener(
        @RequestBody RegisterDTO request
    ) {
        User user = convertToUser(request);
        AuthenticationService context = new AuthenticationService(hulpverlenerRegistration);
        return ResponseEntity.ok(context.executeStrategy(user, Role.HULPVERLENER));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationDTO> login(
            @RequestBody User request
    ) throws CustomAuthenticationException {
        return ResponseEntity.ok(authservice.authenticate(request));
    }

    private User convertToUser(RegisterDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setInfix(dto.getInfix());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(Role.CLIENT);

        return user;
    }
}
