package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Questionary.QuestionaryDAO;
import ngo.spine.eigenschuldapi.Model.Questionary;
import ngo.spine.eigenschuldapi.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionaryAccessFilterTest {

    private QuestionaryAccessFilter SUT;
    private String[] splitRequest;
    private User requestUser;

    @Mock private QuestionaryDAO questionaryDAO;
    @Mock private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void init() {
        this.SUT = new QuestionaryAccessFilter(questionaryDAO);
        this.splitRequest = new String[4];

        this.splitRequest[0] = "api";
        this.splitRequest[1] = "v1";
        this.splitRequest[2] = "questionary";

        this.requestUser = new User();
        this.requestUser.setId(UUID.randomUUID());
    }

    @Test
    void caregiver_can_post_quetions() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        User client = new User();
        client.setHulpverlener(requestUser);

        Questionary questionary = new Questionary(client);

        when(questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);
        when(this.httpServletRequest.getMethod()).thenReturn("POST");

        boolean hasRights = this.SUT.hasRights(this.httpServletRequest, requestUser, splitRequest);

        assertTrue(hasRights);
    }

    @Test
    void user_can_not_post_question() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        User caregiver = new User();
        caregiver.setId(UUID.randomUUID());

        User client = new User();
        client.setHulpverlener(caregiver);

        Questionary questionary = new Questionary(client);

        when(questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);
        when(this.httpServletRequest.getMethod()).thenReturn("POST");

        boolean hasRights = this.SUT.hasRights(this.httpServletRequest, requestUser, splitRequest);

        assertFalse(hasRights);
    }

    @Test
    void client_cant_change_own_questions() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        User caregiver = new User();
        caregiver.setId(UUID.randomUUID());

        User client = new User();
        client.setHulpverlener(caregiver);

        Questionary questionary = new Questionary(client);

        when(questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);
        when(this.httpServletRequest.getMethod()).thenReturn("POST");

        boolean hasRights = this.SUT.hasRights(this.httpServletRequest, client, splitRequest);

        assertFalse(hasRights);
    }

    @Test
    void client_can_get_questionary() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        User caregiver = new User();
        caregiver.setId(UUID.randomUUID());

        User client = new User();
        client.setId(UUID.randomUUID());
        client.setHulpverlener(caregiver);

        Questionary questionary = new Questionary(client);

        when(questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);
        when(this.httpServletRequest.getMethod()).thenReturn("GET");

        boolean hasRights = this.SUT.hasRights(this.httpServletRequest, client, splitRequest);

        assertTrue(hasRights);
    }

    @Test
    void caregiver_can_get_questionary() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        User client = new User();
        client.setId(UUID.randomUUID());
        client.setHulpverlener(requestUser);

        Questionary questionary = new Questionary(client);

        when(questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);
        when(this.httpServletRequest.getMethod()).thenReturn("GET");

        boolean hasRights = this.SUT.hasRights(this.httpServletRequest, requestUser, splitRequest);

        assertTrue(hasRights);
    }
}
