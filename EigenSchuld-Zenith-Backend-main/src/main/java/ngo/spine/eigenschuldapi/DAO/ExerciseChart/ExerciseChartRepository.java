package ngo.spine.eigenschuldapi.DAO.ExerciseChart;

import ngo.spine.eigenschuldapi.Model.ChartOverview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ExerciseChartRepository extends JpaRepository<ChartOverview, UUID> {
    @Query("SELECT co FROM ChartOverview co WHERE co.chartFeeling.id = :chartId OR co.chartSense.id = :chartId")
    ChartOverview findByChartId(UUID chartId);
}
