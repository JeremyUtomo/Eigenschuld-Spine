package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Domain.*;
import ngo.spine.eigenschuldapi.DAO.InvitationToken.*;
import ngo.spine.eigenschuldapi.DAO.User.*;
import ngo.spine.eigenschuldapi.Model.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

@Service
public class EmailService{

    private JavaMailSender javaMailSender;
    private final DomainDAO domainDAO;
    private final UserDAO userDAO;
    private final InvitationTokenDAO invitationTokenDAO;
    private String url = "http://localhost:4200/register/";

    @Autowired
    public EmailService(
        JavaMailSender javaMailSender,
        DomainDAO domainDAO,
        UserDAO userDAO,
        InvitationTokenDAO invitationTokenDAO
    ) {
        this.javaMailSender = javaMailSender;
        this.domainDAO = domainDAO;
        this.userDAO = userDAO;
        this.invitationTokenDAO = invitationTokenDAO;
    }

    public void sendEmail(String email, UUID hulpverlenerId) {
        if (!isValidEmail(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email is not formatted correctly");
        }

        Optional<User> userOptional = userDAO.getById(hulpverlenerId);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        User user = userOptional.get();

        Optional<Domain> domainOptional = domainDAO.getById(user.getDomainId().toString());
        if (domainOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Domain of User not found");
        }
        Domain domain = domainOptional.get();

        SimpleMailMessage message = createMessage(domain, email, user);
        javaMailSender.send(message);
    }

    public SimpleMailMessage createMessage(Domain domain, String email, User user){
        String link = createLink(user);
        String emailTemplate = readFile();
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(domain.getName());
        message.setTo(email);
        message.setSubject("Uitnodiging voor Eigen Schuld");
        message.setText(
            "Register using this link: " + this.url + link + "\n\n" +
            emailTemplate +
            user.getFirstName() + " " + user.getLastName()
        );
        return (message);
    }

    public String createLink(User hulpverlener) {
        String token = UUID.randomUUID().toString();
        InvitationToken invitationToken = new InvitationToken();
        invitationToken.setInviteToken(token);
        invitationToken.setHulpverlener(hulpverlener);
        invitationToken.setExpiryDate(LocalDateTime.now().plusDays(7));
        invitationTokenDAO.save(invitationToken);

        return token;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private String readFile(){
        String emailTemplate;
        try {
            Path path = Paths.get("src/main/resources/templates/email.txt");
            emailTemplate = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read email template", e);
        }
        return emailTemplate;
    }
}
