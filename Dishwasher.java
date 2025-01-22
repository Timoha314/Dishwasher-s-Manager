import java.util.Date;

public class Dishwasher extends Appliance {
    public Dishwasher(int id, String type, String model, double enginePower, double maxSpeed, Date releaseDate, double price) {
        this.id = id;
        this.type = type;
        this.model = model;
        this.enginePower = enginePower;
        this.maxSpeed = maxSpeed;
        this.releaseDate = releaseDate;
        this.price = price;
    }

    public double getEnginePower() {
        return enginePower;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    @Override
    public String toString() {
        return "Dishwasher [ID=" + id + ", Type=" + type + ", Model=" + model +
                ", Engine Power=" + enginePower + "W, Max Speed=" + maxSpeed +
                "km/h, Release Date=" + releaseDate + ", Price=" + price + "]";
    }
}
