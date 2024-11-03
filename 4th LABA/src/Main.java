import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DishwasherList dishwasherList = new DishwasherList();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Чтение данных из файла при запуске
        List<Dishwasher> loadedDishwashers = FileHandler.readFromFile("appliances.txt");
        loadedDishwashers.forEach(dishwasherList::addDishwasher);

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Добавить посудомоечную машину");
            System.out.println("2. Редактировать посудомоечную машину");
            System.out.println("3. Удалить посудомоечную машину");
            System.out.println("4. Показать все посудомоечные машины");
            System.out.println("5. Сортировать по цене");
            System.out.println("6. Записать данные в файл");
            System.out.println("7. Загрузить данные из файла");
            System.out.println("8. Выйти");
            System.out.print("Выберите действие: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера после nextInt

            switch (choice) {
                case 1:
                    try {
                        System.out.print("Введите ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // переход на следующую строку
                        System.out.print("Введите тип: ");
                        String type = scanner.nextLine();
                        System.out.print("Введите модель: ");
                        String model = scanner.nextLine();
                        System.out.print("Введите мощность двигателя: ");
                        double enginePower = scanner.nextDouble();
                        System.out.print("Введите максимальную скорость: ");
                        double maxSpeed = scanner.nextDouble();
                        scanner.nextLine(); // переход на следующую строку
                        System.out.print("Введите дату выпуска (yyyy-MM-dd): ");
                        Date releaseDate = dateFormat.parse(scanner.nextLine());
                        System.out.print("Введите цену: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine(); // переход на следующую строку

                        Dishwasher dishwasher = new Dishwasher(id, type, model, enginePower, maxSpeed, releaseDate, price);
                        dishwasherList.addDishwasher(dishwasher);
                        System.out.println("Посудомоечная машина добавлена.");
                    } catch (ParseException e) {
                        System.out.println("Ошибка: Неправильный формат даты.");
                    }
                    break;
                case 2:
                    System.out.print("Введите ID посудомоечной машины для редактирования: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine(); // переход на следующую строку
                    updateDishwasher(dishwasherList, updateId, scanner);
                    break;
                case 3:
                    System.out.print("Введите ID посудомоечной машины для удаления: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine(); // переход на следующую строку
                    dishwasherList.removeDishwasher(deleteId);
                    System.out.println("Посудомоечная машина удалена.");
                    break;
                case 4:
                    System.out.println("Список всех посудомоечных машин:");
                    dishwasherList.displayAll();
                    break;
                case 5:
                    dishwasherList.sortByPrice();
                    System.out.println("Список отсортирован по цене.");
                    break;
                case 6:
                    FileHandler.writeToFile("appliances.txt", dishwasherList.getDishwashers());
                    System.out.println("Данные записаны в файл.");
                    break;
                case 7:
                    dishwasherList.getDishwashers().clear(); // очистка текущих данных
                    List<Dishwasher> newLoadedDishwashers = FileHandler.readFromFile("appliances.txt");
                    newLoadedDishwashers.forEach(dishwasherList::addDishwasher);
                    System.out.println("Данные загружены из файла.");
                    break;
                case 8:
                    System.out.println("Выход из программы.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
            }
        }
    }

    private static void updateDishwasher(DishwasherList dishwasherList, int id, Scanner scanner) {
        for (Dishwasher dishwasher : dishwasherList.getDishwashers()) {
            if (dishwasher.getId() == id) {
                System.out.println("Введите новый тип:");
                dishwasher.setType(scanner.nextLine());
                System.out.println("Введите новую модель:");
                dishwasher.setModel(scanner.nextLine());
                System.out.println("Введите новую мощность двигателя:");
                dishwasher.setEnginePower(scanner.nextDouble());
                System.out.println("Введите новую максимальную скорость:");
                dishwasher.setMaxSpeed(scanner.nextDouble());
                System.out.println("Введите новую цену:");
                dishwasher.setPrice(scanner.nextDouble());
                scanner.nextLine(); // Для перехода на следующую строку
                System.out.println("Обновлено: " + dishwasher);
                return;
            }
        }
        System.out.println("Посудомоечная машина с ID " + id + " не найдена.");
    }
}
