package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.Model.User;
import ngo.spine.eigenschuldapi.Services.UserService;
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
class UserAccessFilterTest {

    private UserAccessFilter SUT;
    private String[] splitRequest;
    private User requestUser;

    @Mock private UserService userService;
    @Mock private HttpServletRequest httpServletRequest;

    @BeforeEach
    public void init() {
        this.SUT = new UserAccessFilter(userService);

        this.splitRequest = new String[4];

        this.splitRequest[0] = "api";
        this.splitRequest[1] = "v1";
        this.splitRequest[2] = "user";

        this.requestUser = new User();
        this.requestUser.setId(UUID.randomUUID());
    }

    @Test
    void user_can_access() {
        this.splitRequest[3] = this.requestUser.getId().toString();

        try {
            when(userService.getUserById(any(String.class))).thenReturn(this.requestUser);
            boolean result = this.SUT.hasRights(httpServletRequest, requestUser, splitRequest);
            assertTrue(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void caregiver_can_access_client_data() {
        this.splitRequest[3] = this.requestUser.getId().toString();
        User hulpverlener = new User();
        hulpverlener.setId(UUID.randomUUID());

        this.requestUser.setHulpverlener(hulpverlener);

        try {
            when(userService.getUserById(any(String.class))).thenReturn(this.requestUser);
            boolean result = this.SUT.hasRights(httpServletRequest, hulpverlener, splitRequest);
            assertTrue(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void user_can_not_access_user_data() {
        this.splitRequest[3] = this.requestUser.getId().toString();
        User randomUser = new User();
        randomUser.setId(UUID.randomUUID());

        try {
            when(userService.getUserById(any(String.class))).thenReturn(this.requestUser);
            boolean result = this.SUT.hasRights(httpServletRequest, randomUser, splitRequest);
            assertFalse(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
