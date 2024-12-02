package ngo.spine.eigenschuldapi.DTO;

import ngo.spine.eigenschuldapi.Model.Chart;

import java.util.UUID;

public class ChartOverviewDTO {
    public UUID id;
    public Chart chartFeeling;
    public Chart chartSense;

    public ChartOverviewDTO(UUID id, Chart chartFeeling, Chart chartSense) {
        this.id = id;
        this.chartFeeling = chartFeeling;
        this.chartSense = chartSense;
    }
}
