package ngo.spine.eigenschuldapi.Filter.ExerciseAccessFilters;

import jakarta.servlet.http.HttpServletRequest;
import ngo.spine.eigenschuldapi.DAO.Chart.ChartDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseChart.ExerciseChartDAO;
import ngo.spine.eigenschuldapi.Exception.UserDoesNotHaveRightsException;
import ngo.spine.eigenschuldapi.Model.Chart;
import ngo.spine.eigenschuldapi.Model.ChartOverview;
import ngo.spine.eigenschuldapi.Model.User;
import ngo.spine.eigenschuldapi.Services.ChartService;

import java.util.UUID;

public class ChartAccessFilter extends ExerciseAccessFilter {

    private final ChartDAO chartDAO;
    private final ChartService chartService;

    public ChartAccessFilter(ChartDAO chartDAO, ChartService chartService) {
        this.chartDAO = chartDAO;
        this.chartService = chartService;
    }

    @Override
    public boolean hasRights(HttpServletRequest request, User requestUser, String[] splitRequest) throws UserDoesNotHaveRightsException {
        String chartId = splitRequest[splitRequest.length - 1];
        User owner = null;

        if (request.getMethod().equals("GET")) {
            ChartOverview chartOverview = this.chartService.getChartById(chartId);
            owner = chartOverview.getUser();
        } else if (request.getMethod().equals("POST")) {
            Chart chart = chartDAO.getChartById(UUID.fromString(chartId));
            owner = chart.getUser();
        }

        if (owner != null) {
            return this.isUserOrIsCaregiverOfClient(owner, requestUser);
        }

        return false;
    }
}
