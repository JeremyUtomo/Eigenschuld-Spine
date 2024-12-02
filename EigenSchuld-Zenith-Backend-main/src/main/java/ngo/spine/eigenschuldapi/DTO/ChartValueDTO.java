package ngo.spine.eigenschuldapi.DTO;

public class ChartValueDTO {
    private String name;
    private String percentage;
    private String color;

    public ChartValueDTO(String name, String percentage, String color) {
        this.name = name;
        this.percentage = percentage;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getPercentage() {
        return percentage;
    }

    public String getColor() {
        return color;
    }
}
