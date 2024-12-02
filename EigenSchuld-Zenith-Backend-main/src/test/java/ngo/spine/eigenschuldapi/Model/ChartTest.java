package ngo.spine.eigenschuldapi.Model;

import ngo.spine.eigenschuldapi.DTO.ChartValueDTO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ChartTest {

    public Chart SUT;

    @BeforeEach
    public void setup() {
        this.SUT = new Chart();
    }

    @Test
    void set_3_new_values() {
        int expectedResult = 3;

        ChartValueDTO chartValueDTO1 = new ChartValueDTO("Hidde", "20","#FFF");
        ChartValueDTO chartValueDTO2 = new ChartValueDTO("Luuk", "20","#FFF");
        ChartValueDTO chartValueDTO3 = new ChartValueDTO("Marc", "20","#FFF");

        ArrayList<ChartValueDTO> values = new ArrayList<>();
        values.add(chartValueDTO1);
        values.add(chartValueDTO2);
        values.add(chartValueDTO3);

        this.SUT.setNewValues(values);

        int actualResult = this.SUT.values.size();
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void should_throw_exception_when_chart_value_empty() {
        ChartValueDTO chartValueDTO1 = new ChartValueDTO("", "20", "#FFF");
        ChartValueDTO chartValueDTO2 = new ChartValueDTO("Luuk", "20", "#FFF");
        ChartValueDTO chartValueDTO3 = new ChartValueDTO("Marc", "20", "#FFF");

        ArrayList<ChartValueDTO> values = new ArrayList<>();
        values.add(chartValueDTO1);
        values.add(chartValueDTO2);
        values.add(chartValueDTO3);

        RuntimeException thrown = assertThrows(
            RuntimeException.class,
            () -> this.SUT.setNewValues(values),
            "All values must be filled in order to save!"
        );

        assertTrue(thrown.getMessage().contains("All values must be filled in order to save!"));
    }

    @Test
    void should_not_throw_exception_when_values_empty() {
        int expected = 0;

        ArrayList<ChartValueDTO> values = new ArrayList<>();
        this.SUT.setNewValues(values);


        int actual = this.SUT.values.size();
        assertEquals(expected, actual);
    }
}

