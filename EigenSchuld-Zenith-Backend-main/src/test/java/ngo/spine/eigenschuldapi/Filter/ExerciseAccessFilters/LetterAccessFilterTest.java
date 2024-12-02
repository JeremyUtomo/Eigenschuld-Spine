package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Letter.LetterDAO;
import ngo.spine.eigenschuldapi.Model.Letter;
import ngo.spine.eigenschuldapi.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LetterAccessFilterTest {

    private LetterAccessFilter SUT;
    private User client;
    private User caregiver;
    private Letter letter;
    private String[] splitRequest = new String[5];

    @Mock private LetterDAO letterDAO;
    @Mock private HttpServletRequest request;

    @BeforeEach
    void init() {
        this.SUT = new LetterAccessFilter(letterDAO);

        this.caregiver = new User();
        this.caregiver.setId(UUID.randomUUID());

        this.client = new User();
        this.client.setId(UUID.randomUUID());
        this.client.setHulpverlener(caregiver);

        this.letter = new Letter(client);

        this.splitRequest[4] = UUID.randomUUID().toString();
    }

    @Test
    void client_can_access_own_letter() {

        when(letterDAO.getLetterById(any(UUID.class))).thenReturn(this.letter);
        boolean result = this.SUT.hasRights(request, client, splitRequest);

        assertTrue(result);
    }

    @Test
    void caregiver_can_access_his_client_letter() {

        when(letterDAO.getLetterById(any(UUID.class))).thenReturn(this.letter);
        boolean result = this.SUT.hasRights(request, caregiver, splitRequest);

        assertTrue(result);
    }

    @Test
    void random_user_can_not_access_letter() {
        User randomUser = new User();
        randomUser.setId(UUID.randomUUID());

        when(letterDAO.getLetterById(any(UUID.class))).thenReturn(this.letter);
        boolean result = this.SUT.hasRights(request, randomUser, splitRequest);

        assertFalse(result);
    }

}
