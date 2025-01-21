import java.util.Date;

public class DishwasherBuilder {
    private int id;
    private String type;
    private String model;
    private double enginePower;
    private double maxSpeed;
    private Date releaseDate;
    private double price;

    public DishwasherBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public DishwasherBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public DishwasherBuilder setModel(String model) {
        this.model = model;
        return this;
    }

    public DishwasherBuilder setEnginePower(double enginePower) {
        this.enginePower = enginePower;
        return this;
    }

    public DishwasherBuilder setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
        return this;
    }

    public DishwasherBuilder setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public DishwasherBuilder setPrice(double price) {
        this.price = price;
        return this;
    }

    public Dishwasher build() {
        return new Dishwasher(id, type, model, enginePower, maxSpeed, releaseDate, price);
    }
}
