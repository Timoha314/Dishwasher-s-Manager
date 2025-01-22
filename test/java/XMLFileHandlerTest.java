import org.junit.jupiter.api.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class XMLFileHandlerTest {
    private static final String TEMP_XML_FILE = "temp_dishwashers.xml";

    @AfterEach
    void cleanUp() {
        new File(TEMP_XML_FILE).delete();
    }

    @Test
    void testWriteAndReadXMLFile() {
        List<Dishwasher> originalDishwashers = Arrays.asList(
                new Dishwasher(1, "Built-in", "Model A", 1200, 5, new Date(), 500),
                new Dishwasher(2, "Freestanding", "Model B", 1300, 6, new Date(), 600)
        );

        XMLFileHandler.writeToFile(TEMP_XML_FILE, originalDishwashers);
        List<Dishwasher> readDishwashers = XMLFileHandler.readFromFile(TEMP_XML_FILE);

        assertEquals(originalDishwashers.size(), readDishwashers.size());
        assertEquals(originalDishwashers, readDishwashers);
    }

    @Test
    void testReadInvalidXMLFile() {
        File file = new File(TEMP_XML_FILE);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("<invalid>XML</invalid>");
        } catch (Exception e) {
            fail("Test setup failed");
        }

        assertThrows(RuntimeException.class, () -> XMLFileHandler.readFromFile(TEMP_XML_FILE));
    }
}
