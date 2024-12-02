package ngo.spine.eigenschuldapi.Controller;

import ngo.spine.eigenschuldapi.DTO.ChartOverviewDTO;
import ngo.spine.eigenschuldapi.DTO.ChartValueDTO;
import ngo.spine.eigenschuldapi.Filter.HasRightsToUse;
import ngo.spine.eigenschuldapi.Model.Chart;
import ngo.spine.eigenschuldapi.Model.ChartOverview;
import ngo.spine.eigenschuldapi.Services.ChartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/v1/chart")
public class ChartController {

    private final ChartService chartService;

    @Autowired
    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @HasRightsToUse
    @GetMapping("/{exerciseProgressId}")
    public ResponseEntity<ChartOverviewDTO> getChartsForExerciseWithId(@PathVariable String exerciseProgressId) {
        ChartOverview chartOverview = this.chartService.getChartById(exerciseProgressId);

        ChartOverviewDTO response = new ChartOverviewDTO(
            chartOverview.id,
            chartOverview.getChartFeeling(),
            chartOverview.getChartSense()
        );

        return ResponseEntity.ok(response);
    }

    @HasRightsToUse
    @PostMapping("/save/{chartId}")
    public ResponseEntity<Chart> addValueToChartWithId(
        @PathVariable String chartId,
        @RequestBody ArrayList<ChartValueDTO> requestBody
    ) {
        return this.chartService.addValueToChartWithId(chartId, requestBody);
    }
}
