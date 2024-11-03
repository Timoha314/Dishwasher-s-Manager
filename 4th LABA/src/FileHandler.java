import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileHandler {
    public static void writeToFile(String filePath, List<Dishwasher> dishwashers) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Dishwasher dishwasher : dishwashers) {
                bw.write(dishwasher.getId() + "," + dishwasher.getType() + "," + dishwasher.getModel() + "," +
                        dishwasher.getEnginePower() + "," + dishwasher.getMaxSpeed() + "," +
                        new SimpleDateFormat("yyyy-MM-dd").format(dishwasher.getReleaseDate()) + "," +
                        dishwasher.getPrice());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static List<Dishwasher> readFromFile(String filePath) {
        List<Dishwasher> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String type = parts[1];
                String model = parts[2];
                double enginePower = Double.parseDouble(parts[3]);
                double maxSpeed = Double.parseDouble(parts[4]);
                Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(parts[5]);
                double price = Double.parseDouble(parts[6]);
                list.add(new Dishwasher(id, type, model, enginePower, maxSpeed, releaseDate, price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
