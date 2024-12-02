package ngo.spine.eigenschuldapi.Seeder;

import ngo.spine.eigenschuldapi.DAO.InvitationToken.InvitationTokenDAO;
import ngo.spine.eigenschuldapi.DAO.User.UserDAO;
import ngo.spine.eigenschuldapi.Interface.*;
import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.Services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class UserSeeder {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserDAO userDAO;
    private final RegisterStrategy clientRegistration;

    public UserSeeder(
        AuthenticationService authenticationService,
        EmailService emailService, UserDAO userDAO,
        @Qualifier("clientRegistrationService") RegisterStrategy clientRegistration
    ) {
        this.authenticationService = authenticationService;
        this.emailService = emailService;
        this.userDAO = userDAO;
        this.clientRegistration = clientRegistration;
    }

    public User seed(User hulpverlener) {

        String link = createLink(hulpverlener);

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setPassword("hallo123");
        user.setEmail("user@gmail.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        authenticationService = new AuthenticationService(clientRegistration);
        this.authenticationService.executeStrategy(user,
            Role.CLIENT,
            link
        );
        return user;
    }

    private String createLink(User hulpverlener) {
        Optional<User> userOptional = this.userDAO.getUserByEmail(hulpverlener.getEmail());
        userOptional.ifPresent(user -> hulpverlener.setId(user.getId()));
        String link = this.emailService.createLink(hulpverlener);
        return link;
    }
}
