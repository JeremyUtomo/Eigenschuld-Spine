package ngo.spine.eigenschuldapi.Services;

import ngo.spine.eigenschuldapi.DAO.Chart.ChartDAO;
import ngo.spine.eigenschuldapi.DAO.ExerciseChart.ExerciseChartDAO;
import ngo.spine.eigenschuldapi.DTO.ChartValueDTO;
import ngo.spine.eigenschuldapi.Model.Chart;
import ngo.spine.eigenschuldapi.Model.ChartOverview;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
public class ChartService {

    private final ChartDAO chartDAO;
    private final ExerciseChartDAO exerciseChartDAO;
    private final ExerciseProgressService exerciseProgressService;

    @Autowired
    public ChartService(ChartDAO chartDAO, ExerciseChartDAO exerciseChartDAO, ExerciseProgressService exerciseProgressService) {
        this.exerciseChartDAO = exerciseChartDAO;
        this.chartDAO = chartDAO;
        this.exerciseProgressService = exerciseProgressService;
    }

    public ChartOverview getChartById(String id) {
        ChartOverview chart = this.exerciseChartDAO.getById(id);
        this.exerciseProgressService.setDateForExerciseProgress(id);

        if(chart != null) {
            return chart;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
    }

    @Transactional
    public ResponseEntity<Chart> addValueToChartWithId(String chartId, ArrayList<ChartValueDTO> requestBody) {
        Chart chart = this.chartDAO.getChartById(UUID.fromString(chartId));
        ChartOverview chartOverview = this.exerciseChartDAO.getChartOverviewByChartId(UUID.fromString(chartId));
        this.exerciseProgressService.setIsDoneForExerciseProgress(chartOverview.id);

        if (chart != null) {
            try {
                chart.setNewValues(requestBody);
                chartDAO.save(chart);
                return ResponseEntity.ok(chart);
            } catch (RuntimeException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find chart!");
    }
}
