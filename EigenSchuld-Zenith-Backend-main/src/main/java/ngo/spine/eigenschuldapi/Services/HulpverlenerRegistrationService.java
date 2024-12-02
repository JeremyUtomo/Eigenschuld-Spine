package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Domain.*;
import ngo.spine.eigenschuldapi.DTO.AuthenticationDTO;
import ngo.spine.eigenschuldapi.Interface.*;
import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.DAO.User.UserRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.*;

import java.util.*;
import java.util.regex.*;

@Component("hulpverlenerRegistrationService")
public class HulpverlenerRegistrationService implements RegisterStrategy {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DomainDAO domainDAO;
    private final UserService userService;
    private final RegisterErrorService registerErrorService;

    @Autowired
    public HulpverlenerRegistrationService(
        UserRepository repository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        DomainDAO domainDAO,
        UserService userService,
        RegisterErrorService registerErrorService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.domainDAO = domainDAO;
        this.userService = userService;
        this.registerErrorService = registerErrorService;
    }

    @Override
    public AuthenticationDTO register(User request, Role role, String token) {
        String email = request.getEmail();
        Optional<User> existingUser = repository.findUserByEmail(email);
        UUID domainId = this.checkForDomain(email);

        this.registerErrorService.validateUserDoesNotExist(existingUser);
        this.validateDomainIsRegistered(domainId);
        this.registerErrorService.validatePassword(request.getPassword());

        User user = userService.createUser(request, role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDomainId(domainId);

        user = repository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return new AuthenticationDTO(jwtToken, user.getId());
    }

    private UUID checkForDomain(String email) {
        List<Domain> domains = domainDAO.getAllDomains();

        if (email == null) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Email is null");
        }

        for (Domain domain : domains) {
            String regex = ".*@" + Pattern.quote(domain.getName()) + ".*";
            if (Pattern.compile(regex).matcher(email).matches()) {
                return domain.getId();
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "email domain is not registered");
    }

    private void validateDomainIsRegistered(UUID domainId) {
        if (domainId == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Domain not registered");
        }
    }
}
