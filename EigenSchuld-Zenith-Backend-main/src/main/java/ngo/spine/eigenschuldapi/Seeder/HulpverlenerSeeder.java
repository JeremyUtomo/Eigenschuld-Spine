package ngo.spine.eigenschuldapi.Seeder;

import ngo.spine.eigenschuldapi.Interface.*;
import ngo.spine.eigenschuldapi.Model.Domain;
import ngo.spine.eigenschuldapi.Model.Role;
import ngo.spine.eigenschuldapi.Model.User;
import ngo.spine.eigenschuldapi.Services.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
public class HulpverlenerSeeder {

    @Autowired
    private AuthenticationService authenticationService;
    private final RegisterStrategy hulpverlenerRegistration;

    public HulpverlenerSeeder(
        AuthenticationService authenticationService,
        @Qualifier("hulpverlenerRegistrationService") RegisterStrategy hulpverlenerRegistration
    ) {
        this.authenticationService = authenticationService;
        this.hulpverlenerRegistration = hulpverlenerRegistration;
    }

    public User seed(Domain domain) {
        User user = new User();
        user.setPassword("hallo123");
        user.setEmail("hulp@gmail.com");
        user.setDomainId(domain.getId());
        user.setFirstName("John");
        user.setLastName("Doe");

        authenticationService = new AuthenticationService(hulpverlenerRegistration);
        this.authenticationService.executeStrategy(user,Role.HULPVERLENER, null);

        return user;
    }
}
