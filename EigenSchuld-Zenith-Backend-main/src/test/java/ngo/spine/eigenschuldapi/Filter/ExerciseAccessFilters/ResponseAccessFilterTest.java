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

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ResponseAccessFilterTest {

    private ResponseAccessFilter SUT;
    private String[] splitRequest;
    private User client;
    private User caregiver;

    @Mock
    private QuestionaryDAO questionaryDAO;
    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void init() {
        this.SUT = new ResponseAccessFilter(questionaryDAO);
        this.splitRequest = new String[4];

        this.splitRequest[0] = "api";
        this.splitRequest[1] = "v1";
        this.splitRequest[2] = "questionary";

        this.caregiver = new User();
        this.caregiver.setId(UUID.randomUUID());

        this.client = new User();
        this.client.setId(UUID.randomUUID());
        this.client.setHulpverlener(this.caregiver);
    }

    @Test
    void client_can_add_response() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        Questionary questionary = new Questionary(client);

        when(this.questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);

        try {
            boolean result = this.SUT.hasRights(httpServletRequest, client, splitRequest);
            assertTrue(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void caregiver_can_add_response() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        Questionary questionary = new Questionary(client);

        when(this.questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);

        try {
            boolean result = this.SUT.hasRights(httpServletRequest, caregiver, splitRequest);
            assertTrue(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void user_can_not_add_response() {
        UUID questionaryId = UUID.randomUUID();
        this.splitRequest[3] = questionaryId.toString();

        User user = new User();
        user.setId(UUID.randomUUID());

        Questionary questionary = new Questionary(client);

        when(this.questionaryDAO.getById(any(UUID.class))).thenReturn(questionary);

        try {
            boolean result = this.SUT.hasRights(httpServletRequest, user, splitRequest);
            assertFalse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
