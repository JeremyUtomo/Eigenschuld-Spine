package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Chart.ChartDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseChart.ExerciseChartDAO;
import ngo.spine.eigenschuldapi.Model.Chart;
import ngo.spine.eigenschuldapi.Model.ChartOverview;
import ngo.spine.eigenschuldapi.Model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ChartAccessFilterTest {

    private ChartAccessFilter SUT;
    private User client;
    private User caregiver;
    private String[] splitRequest = new String[5];

    @Mock private ChartDAO chartDAO;
    @Mock private ExerciseChartDAO exerciseChartDAO;
    @Mock private HttpServletRequest request;

    @BeforeEach
    void init() {
        this.SUT = new ChartAccessFilter(chartDAO, exerciseChartDAO);

        this.caregiver = new User();
        this.caregiver.setId(UUID.randomUUID());

        this.client = new User();
        this.client.setId(UUID.randomUUID());
        this.client.setHulpverlener(caregiver);

        this.splitRequest[4] = UUID.randomUUID().toString();
    }

    @Test
    void client_can_access_own_chart_overview() {
        ChartOverview chartOverview = new ChartOverview(client);

        when(request.getMethod()).thenReturn("GET");
        when(exerciseChartDAO.getById(any(String.class))).thenReturn(chartOverview);

        try {
            boolean hasRights = SUT.hasRights(request, client, splitRequest);
            assertTrue(hasRights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void caregiver_can_access_own_client_chart_overview() {
        ChartOverview chartOverview = new ChartOverview(client);

        when(request.getMethod()).thenReturn("GET");
        when(exerciseChartDAO.getById(any(String.class))).thenReturn(chartOverview);

        try {
            boolean hasRights = SUT.hasRights(request, caregiver, splitRequest);
            assertTrue(hasRights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void user_can_not_access_chart_overview() {
        ChartOverview chartOverview = new ChartOverview(client);

        User randUser = new User();
        randUser.setId(UUID.randomUUID());

        when(request.getMethod()).thenReturn("GET");
        when(exerciseChartDAO.getById(any(String.class))).thenReturn(chartOverview);

        try {
            boolean hasRights = SUT.hasRights(request, randUser, splitRequest);
            assertFalse(hasRights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void client_can_change_own_chart() {
        Chart chart = new Chart(client);

        when(request.getMethod()).thenReturn("POST");
        when(chartDAO.getChartById(any(UUID.class))).thenReturn(chart);

        try {
            boolean hasRights = SUT.hasRights(request, client, splitRequest);
            assertTrue(hasRights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void user_can_not_post_chart() {
        Chart chart = new Chart(client);

        User randUser = new User();
        randUser.setId(UUID.randomUUID());

        when(request.getMethod()).thenReturn("POST");
        when(chartDAO.getChartById(any(UUID.class))).thenReturn(chart);

        try {
            boolean hasRights = SUT.hasRights(request, randUser, splitRequest);
            assertFalse(hasRights);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
