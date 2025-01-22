import java.util.ArrayList;
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

    // Сортировка по цене
    public void sortByPrice() {
        dishwashers.sort(Comparator.comparingDouble(Appliance::getPrice));
    }

    // Сортировка по скорости
    public void sortBySpeed() {
        dishwashers.sort(Comparator.comparingDouble(Dishwasher::getMaxSpeed));
    }

    // Сортировка по мощности двигателя
    public void sortByEnginePower() {
        dishwashers.sort(Comparator.comparingDouble(Dishwasher::getEnginePower));
    }

    public void displayAll() {
        dishwashers.forEach(System.out::println);
    }

    public List<Dishwasher> getDishwashers() {
        return dishwashers;
    }
}
