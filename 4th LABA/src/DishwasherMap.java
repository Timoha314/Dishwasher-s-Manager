import java.util.HashMap;
import java.util.Map;

public class DishwasherMap {
    private Map<Integer, Dishwasher> dishwasherMap;

    public DishwasherMap() {
        dishwasherMap = new HashMap<>();
    }

    public void addDishwasher(Dishwasher dishwasher) {
        dishwasherMap.put(dishwasher.getId(), dishwasher);
    }

    public void removeDishwasher(int id) {
        dishwasherMap.remove(id);
    }

    public void displayAll() {
        dishwasherMap.values().forEach(System.out::println);
    }

    public Map<Integer, Dishwasher> getDishwasherMap() {
        return dishwasherMap;
    }
}
