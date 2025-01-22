import java.util.Date;

public abstract class Appliance {
    protected int id;
    protected String type;
    protected String model;
    protected double enginePower;
    protected double maxSpeed;
    protected Date releaseDate;
    protected double price;

    public abstract String toString();
    public int getId() { return id; }
    public String getType() { return type; }

    public String getModel() { return model; }

    public double getEnginePower() { return enginePower; }

    public double getMaxSpeed() { return maxSpeed; }

    public Date getReleaseDate() { return releaseDate; }

    public double getPrice() { return price; }
}
