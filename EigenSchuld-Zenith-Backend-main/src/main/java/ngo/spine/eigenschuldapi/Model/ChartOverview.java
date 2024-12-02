package ngo.spine.eigenschuldapi.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class ChartOverview {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @OneToOne
    private Chart chartFeeling;

    @OneToOne
    private Chart chartSense;

    @ManyToOne
    private User user;

    public ChartOverview() { }

    public ChartOverview(User user) {
        this.user = user;
    }

    public Chart getChartFeeling() {
        return chartFeeling;
    }

    public void setChartFeeling(Chart chartFeeling) {
        this.chartFeeling = chartFeeling;
    }

    public Chart getChartSense() {
        return chartSense;
    }

    public void setChartSense(Chart chartSense) {
        this.chartSense = chartSense;
    }

    @JsonBackReference
    public User getUser() {
        return user;
    }
}
