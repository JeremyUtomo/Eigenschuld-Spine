package ngo.spine.eigenschuldapi.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import ngo.spine.eigenschuldapi.DTO.ChartValueDTO;
import ngo.spine.eigenschuldapi.Services.AesEncrypter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
public class Chart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public UUID id;

    @OneToMany
    @JsonManagedReference
    public List<Chart.Value> values = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    private User user;

    public Chart() { }

    public Chart(User user) {
        this.user = user;
    }

    @JsonIgnore
    public User getUser() {
        return this.user;
    }

    public void setNewValues(List<ChartValueDTO> values) throws RuntimeException {
        this.values = new ArrayList<>();

        for (ChartValueDTO val : values) {
            if (val.getName().isEmpty() || val.getPercentage().isEmpty() || val.getColor().isEmpty()) {
                throw new RuntimeException("All values must be filled in order to save!");
            }

            this.addNewValue(
                val.getName(),
                Integer.parseInt(val.getPercentage()),
                val.getColor()
            );
        }
    }

    private void addNewValue(String name, int percentage, String color) {
        for (Value value : values) {
            if (value.name.equals(name)) {
                value.percentage = percentage;
                return;
            }
        }

        values.add(new Value(name, percentage, color));
    }

    // Has to be static for the JSON serialization and deserialization
    @Entity(name = "chart_value")
    public static class Value {
        @Id
        @GeneratedValue
        private UUID id;

        @Convert(converter = AesEncrypter.class)
        public String name;
        @Convert(converter = AesEncrypter.class)
        public int percentage;
        public String color;

        public Value() { }

        public Value(String name, int percentage, String color) {
            this.name = name;
            this.percentage = percentage;
            this.color = color;
        }
    }
}


