import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class JSONFileHandlerTest {
    private static final String TEMP_JSON_FILE = "temp_dishwashers.json";

    @AfterEach
    void cleanUp() {
        new File(TEMP_JSON_FILE).delete();
    }

    @Test
    void testWriteAndReadJSONFile() {
        List<Dishwasher> originalDishwashers = Arrays.asList(
                new Dishwasher(1, "Built-in", "Model A", 1200, 5, new Date(), 500),
                new Dishwasher(2, "Freestanding", "Model B", 1300, 6, new Date(), 600)
        );

        JSONFileHandler.writeToFile(TEMP_JSON_FILE, originalDishwashers);
        List<Dishwasher> readDishwashers = JSONFileHandler.readFromFile(TEMP_JSON_FILE);

        assertEquals(originalDishwashers.size(), readDishwashers.size());
        assertEquals(originalDishwashers, readDishwashers);
    }

    @Test
    void testReadCorruptedJSONFile() {
        File file = new File(TEMP_JSON_FILE);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("{corrupted JSON}");
        } catch (Exception e) {
            fail("Test setup failed");
        }

        assertThrows(RuntimeException.class, () -> JSONFileHandler.readFromFile(TEMP_JSON_FILE));
    }

    @Test
    void testReadNonexistentFile() {
        assertThrows(IOException.class, () -> JSONFileHandler.readFromFile("nonexistent.json"));
    }
}
