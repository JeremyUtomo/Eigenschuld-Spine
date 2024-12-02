package ngo.spine.eigenschuldapi.DAO.ExerciseChart;

import ngo.spine.eigenschuldapi.Model.ChartOverview;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ExerciseChartDAO {

    private final ExerciseChartRepository repository;

    @Autowired
    public ExerciseChartDAO(ExerciseChartRepository repository) {
        this.repository = repository;
    }

    public ChartOverview getById(UUID id) {
        return this.repository.findById(id).orElse(null);
    }

    public ChartOverview getById(String id) {
        return this.repository.findById(UUID.fromString(id)).orElse(null);
    }

    public void save(ChartOverview chartOverview) {
        this.repository.save(chartOverview);
    }

    public ChartOverview getChartOverviewByChartId(UUID chartId) {
        return repository.findByChartId(chartId);
    }
}
