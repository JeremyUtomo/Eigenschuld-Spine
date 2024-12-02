package ngo.spine.eigenschuldapi.DAO.Chart;

import ngo.spine.eigenschuldapi.Model.Chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ChartDAO {

    private final ChartRepository chartRepository;
    private final ChartValueRepository valueRepository;

    @Autowired
    public ChartDAO(ChartRepository chartRepository, ChartValueRepository chartValueRepository) {
        this.chartRepository = chartRepository;
        this.valueRepository = chartValueRepository;
    }

    public Chart getChartById(UUID id) {
        Optional<Chart> chart = this.chartRepository.findById(id);
        return chart.orElse(null);
    }

    public void save(Chart chart) {
        valueRepository.saveAll(chart.values);
        chartRepository.save(chart);
    }
}
