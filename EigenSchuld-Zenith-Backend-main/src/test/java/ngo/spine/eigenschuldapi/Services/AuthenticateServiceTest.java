package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Domain.*;
import ngo.spine.eigenschuldapi.DAO.InvitationToken.*;
import ngo.spine.eigenschuldapi.DAO.User.*;
import ngo.spine.eigenschuldapi.DTO.*;
import ngo.spine.eigenschuldapi.Exception.*;
import ngo.spine.eigenschuldapi.Model.*;
import ngo.spine.eigenschuldapi.Model.User;
import org.apache.tomcat.util.net.openssl.ciphers.*;
import org.checkerframework.checker.units.qual.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.web.server.*;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private ExerciseProgressService exerciseProgressService;

    @Mock
    private InvitationTokenDAO invitationTokenDAO;

    @Mock
    private UserService userService;

    @Mock
    private RegisterErrorService registerErrorService;
    @Mock
    private DomainDAO domainDAO;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private ClientRegistrationService clientRegistrationService;
    @InjectMocks
    private HulpverlenerRegistrationService hulpverlenerRegistrationService;
    @InjectMocks
    private AuthenticationService authenticationService;


    private User user;
    private Role role;
    private InvitationToken invitationToken;
    private List <Domain> domains;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setPassword("password123");

        role = Role.CLIENT;

        User hulpverlener = new User();
        hulpverlener.setId(UUID.randomUUID());
        hulpverlener.setEmail("hulp@example.com");
        hulpverlener.setPassword("password123");

        invitationToken = new InvitationToken();
        invitationToken.setId(UUID.randomUUID());
        invitationToken.setInviteToken("valid-token");
        invitationToken.setHulpverlener(hulpverlener);
        invitationToken.setExpiryDate(LocalDateTime.now().plusDays(1));

        domains = new ArrayList<Domain>();

        Domain domain = new Domain();
        domain.setId(UUID.randomUUID());
        domain.setName("example");

        domains.add(domain);
    }

    @Test
    public void should_register_client() {
        when(userRepository.findUserByEmail(user.getEmail()))
            .thenReturn(Optional.empty());

        when(invitationTokenDAO.findByInviteToken("valid-token"))
            .thenReturn(Optional.of(invitationToken));

        when(userService.createUser(user, role))
            .thenReturn(user);

        when(passwordEncoder.encode(user.getPassword()))
            .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
            .thenReturn(user);

        when(jwtService.generateToken(any(User.class)))
            .thenReturn("jwt-token");

        AuthenticationDTO authenticationDTO = clientRegistrationService.register(user, role, "valid-token");

        assertNotNull(authenticationDTO);
        assertEquals("jwt-token", authenticationDTO.getToken());
        assertEquals(user.getId(), authenticationDTO.getUserId());

        verify(userRepository, times(1))
            .findUserByEmail(user.getEmail());
        verify(invitationTokenDAO, times(1))
            .findByInviteToken("valid-token");
        verify(userService, times(1))
            .createUser(user, role);
        verify(passwordEncoder, times(1))
            .encode("password123");
        verify(userRepository, times(1))
            .save(user);
        verify(jwtService, times(1))
            .generateToken(user);
        verify(exerciseProgressService, times(1))
            .addExercisesToClient(user.getId().toString());
        verify(invitationTokenDAO, times(1))
            .deleteById(invitationToken.getId());
    }

    @Test
    public void should_register_hulpverlener() {
        when(userRepository.findUserByEmail(user.getEmail()))
            .thenReturn(Optional.empty());

        when(userService.createUser(user, Role.HULPVERLENER))
            .thenReturn(user);

        when(passwordEncoder.encode(user.getPassword()))
            .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
            .thenReturn(user);

        when(jwtService.generateToken(any(User.class)))
            .thenReturn("jwt-token");

        when(domainDAO.getAllDomains())
            .thenReturn(domains);

        AuthenticationDTO authenticationDTO = hulpverlenerRegistrationService.register(user, Role.HULPVERLENER, null);

        assertNotNull(authenticationDTO);
        assertEquals("jwt-token", authenticationDTO.getToken());
        assertEquals(user.getId(), authenticationDTO.getUserId());

        verify(userRepository, times(1))
            .findUserByEmail(user.getEmail());
        verify(userService, times(1))
            .createUser(user, Role.HULPVERLENER);
        verify(passwordEncoder, times(1))
            .encode("password123");
        verify(userRepository, times(1))
            .save(user);
        verify(jwtService, times(1))
            .generateToken(user);
    }

    @Test
    public void should_throw_user_already_exist() {
        when(userRepository.findUserByEmail(user.getEmail()))
            .thenReturn(Optional.of(user));

        when(invitationTokenDAO.findByInviteToken("valid-token"))
            .thenReturn(Optional.of(invitationToken));

        when(userService.createUser(user, role))
            .thenReturn(user);

        when(passwordEncoder.encode(user.getPassword()))
            .thenReturn("encodedPassword");

        when(userRepository.save(any(User.class)))
            .thenReturn(user);

        when(jwtService.generateToken(any(User.class)))
            .thenReturn("jwt-token");

        clientRegistrationService.register(user, role, invitationToken.getInviteToken());

        verify(registerErrorService, times(1))
            .validateUserDoesNotExist(Optional.of(user));
        verify(registerErrorService, never())
            .validatePassword(user.getPassword());

        verify(userRepository, times(1))
            .findUserByEmail(user.getEmail());
    }

    @Test
    public void should_throw_token_invalid() {
        when(userRepository.findUserByEmail(user.getEmail()))
            .thenReturn(Optional.empty());

        when(invitationTokenDAO.findByInviteToken("invalid-token"))
            .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            clientRegistrationService.register(user, role, "invalid-token");
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(userRepository, times(1))
            .findUserByEmail(user.getEmail());
        verify(invitationTokenDAO, times(1))
            .findByInviteToken("invalid-token");
        verify(registerErrorService, never())
            .validateUserDoesNotExist(any(Optional.class));
        verify(registerErrorService, never())
            .validatePassword(anyString());
        verify(userService, never())
            .createUser(any(User.class), any(Role.class));
        verify(passwordEncoder, never())
            .encode(anyString());
        verify(userRepository, never())
            .save(any(User.class));
        verify(jwtService, never())
            .generateToken(any(User.class));
        verify(exerciseProgressService, never())
            .addExercisesToClient(anyString());
        verify(invitationTokenDAO, never())
            .deleteById(any(UUID.class));
    }

    @Test
    public void should_throw_token_expired() {
        invitationToken.setExpiryDate(LocalDateTime.now().minusDays(1));

        when(userRepository.findUserByEmail(user.getEmail()))
            .thenReturn(Optional.empty());

        when(invitationTokenDAO.findByInviteToken("expired-token"))
            .thenReturn(Optional.of(invitationToken));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            clientRegistrationService.register(user, role, "expired-token");
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

        verify(userRepository, times(1))
            .findUserByEmail(user.getEmail());
        verify(invitationTokenDAO, times(1))
            .findByInviteToken("expired-token");
        verify(invitationTokenDAO, times(1))
            .deleteById(invitationToken.getId());
        verify(registerErrorService, never())
            .validateUserDoesNotExist(any(Optional.class));
        verify(registerErrorService, never())
            .validatePassword(anyString());
        verify(userService, never())
            .createUser(any(User.class), any(Role.class));
        verify(passwordEncoder, never())
            .encode(anyString());
        verify(userRepository, never())
            .save(any(User.class));
        verify(jwtService, never())
            .generateToken(any(User.class));
        verify(exerciseProgressService, never())
            .addExercisesToClient(anyString());
    }

    @Test
    public void should_throw_no_domain_registered() {
        domains.get(0).setName("gmail");
        when(domainDAO.getAllDomains())
            .thenReturn(domains);

        when(userRepository.findUserByEmail(user.getEmail()))
            .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            hulpverlenerRegistrationService.register(user, Role.HULPVERLENER, null);
        });

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());

        verify(userRepository, times(1))
            .findUserByEmail(user.getEmail());
        verify(registerErrorService, never())
            .validateUserDoesNotExist(any(Optional.class));
        verify(registerErrorService, never())
            .validatePassword(anyString());
        verify(userService, never())
            .createUser(any(User.class), any(Role.class));
        verify(passwordEncoder, never())
            .encode(anyString());
        verify(userRepository, never())
            .save(any(User.class));
        verify(jwtService, never())
            .generateToken(any(User.class));
        verify(exerciseProgressService, never())
            .addExercisesToClient(anyString());
    }

    @Test
    public void should_login() throws CustomAuthenticationException {
        User request = new User();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        User user = new User();
        user.setId(UUID.randomUUID());
        when(userDAO.getUserByEmail(request.getUsername()))
            .thenReturn(Optional.of(user));

        String token = "jwt-token";
        when(jwtService.generateToken(user)).thenReturn(token);

        AuthenticationDTO authenticationDTO = authenticationService.authenticate(request);

        assertNotNull(authenticationDTO);
        assertEquals(token, authenticationDTO.getToken());
        assertEquals(user.getId(), authenticationDTO.getUserId());

        verify(authenticationManager, times(1))
            .authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDAO, times(1))
            .getUserByEmail(request.getUsername());
        verify(jwtService, times(1))
            .generateToken(user);
        verify(userDAO, times(1))
            .save(user);
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        User request = new User();
        request.setEmail("nonexistent@example.com");
        request.setPassword("password123");

        when(authenticationManager.authenticate(any()))
            .thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.authenticate(request));

        verify(authenticationManager, times(1))
            .authenticate(any());
        verify(userDAO, never())
            .getUserByEmail(anyString());
        verify(jwtService, never())
            .generateToken(any(User.class));
        verify(userDAO, never())
            .save(any(User.class));
    }
}
