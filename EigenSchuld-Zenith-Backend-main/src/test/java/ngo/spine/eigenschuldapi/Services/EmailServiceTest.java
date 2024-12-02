package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Domain.*;
import ngo.spine.eigenschuldapi.DAO.InvitationToken.*;
import ngo.spine.eigenschuldapi.DAO.User.*;
import ngo.spine.eigenschuldapi.Model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.*;
import org.springframework.web.server.*;

import java.nio.file.*;
import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private DomainDAO domainDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private InvitationTokenDAO invitationTokenDAO;

    @InjectMocks
    private EmailService emailService;

    private User user;
    private Domain domain;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setDomainId(UUID.randomUUID());

        domain = new Domain();
        domain.setId(user.getDomainId());
        domain.setName("Test Domain");
    }

    @Test
    public void should_send_email() {
        String email = "test@example.com";

        when(userDAO.getById(user.getId())).thenReturn(Optional.of(user));
        when(domainDAO.getById(user.getDomainId().toString())).thenReturn(Optional.of(domain));
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        doAnswer(invocation -> {
            InvitationToken token = invocation.getArgument(0);
            assertNotNull(token.getInviteToken());
            assertEquals(user, token.getHulpverlener());
            assertTrue(token.getExpiryDate().isAfter(LocalDateTime.now()));
            return null;
        }).when(invitationTokenDAO).save(any(InvitationToken.class));

        emailService.sendEmail(email, user.getId());

        verify(userDAO, times(1)).getById(user.getId());
        verify(domainDAO, times(1)).getById(user.getDomainId().toString());
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(invitationTokenDAO, times(1)).save(any(InvitationToken.class));
    }

    @Test
    public void should_throw_incorrect_email_format() {
        String invalidEmail = "invalid-email";

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            emailService.sendEmail(invalidEmail, user.getId());
        });

        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getStatusCode());
        assertEquals("Email is not formatted correctly", exception.getReason());

        verify(userDAO, never()).getById(any(UUID.class));
        verify(domainDAO, never()).getById(anyString());
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
        verify(invitationTokenDAO, never()).save(any(InvitationToken.class));
    }

    @Test
    public void should_throw_user_not_found() {
        String email = "test@example.com";

        when(userDAO.getById(user.getId())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            emailService.sendEmail(email, user.getId());
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("User not found", exception.getReason());

        verify(userDAO, times(1)).getById(user.getId());
        verify(domainDAO, never()).getById(anyString());
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
        verify(invitationTokenDAO, never()).save(any(InvitationToken.class));
    }

    @Test
    public void should_throw_domain_not_found() {
        String email = "test@example.com";

        when(userDAO.getById(user.getId())).thenReturn(Optional.of(user));
        when(domainDAO.getById(user.getDomainId().toString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            emailService.sendEmail(email, user.getId());
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Domain of User not found", exception.getReason());

        verify(userDAO, times(1)).getById(user.getId());
        verify(domainDAO, times(1)).getById(user.getDomainId().toString());
        verify(javaMailSender, never()).send(any(SimpleMailMessage.class));
        verify(invitationTokenDAO, never()).save(any(InvitationToken.class));
    }
}
