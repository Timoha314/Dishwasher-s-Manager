import java.io.File;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private final String databasePath;

    private DatabaseConnection() {
        this.databasePath = "dishwashers.txt";
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public File getDatabaseFile() {
        return new File(databasePath);
    }
}
