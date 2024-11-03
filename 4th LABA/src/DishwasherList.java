import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DishwasherList {
    private final List<Dishwasher> dishwashers;

    public DishwasherList() {
        dishwashers = new ArrayList<>();
    }

    public void addDishwasher(Dishwasher dishwasher) {
        dishwashers.add(dishwasher);
    }

    public void removeDishwasher(int id) {
        dishwashers.removeIf(d -> d.getId() == id);
    }

    public void sortByPrice() {
        dishwashers.sort(Comparator.comparingDouble(Appliance::getPrice));
    }

    public void displayAll() {
        dishwashers.forEach(System.out::println);
    }

    public List<Dishwasher> getDishwashers() {
        return dishwashers;
    }

}
