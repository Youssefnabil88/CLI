import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ListHiddenTest {

    private static final String TEST_FILENAME = "file.txt";
    private static final String TEST_HIDDEN_FILENAME = ".hiddenfile";
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
        File file = Paths.get(System.getProperty("user.dir"), TEST_FILENAME).toFile();
        if (file.exists()) {
            file.delete();
        }
        File hiddenFile = Paths.get(System.getProperty("user.dir"), TEST_HIDDEN_FILENAME).toFile();
        if (hiddenFile.exists()) {
            hiddenFile.delete();
        }
    }

    @Test
    void testListDirectoryWithHiddenFiles() {
        File testFile = Paths.get(System.getProperty("user.dir"), TEST_FILENAME).toFile();
        File hiddenFile = Paths.get(System.getProperty("user.dir"), TEST_HIDDEN_FILENAME).toFile();
        
        try {
            assertTrue(testFile.createNewFile(), "Test file should be created.");
            assertTrue(hiddenFile.createNewFile(), "Hidden test file should be created.");
        } catch (Exception e) {
            fail("Failed to create test files: " + e.getMessage());
        }

        String[] commandArgs = {"ls", "-a"};
        CommandExecution.listDirectoryWithHidden(commandArgs);

        String output = outputStreamCaptor.toString().trim();
        assertTrue(output.contains(TEST_FILENAME), "Output should contain the test file.");
        assertTrue(output.contains(TEST_HIDDEN_FILENAME), "Output should contain the hidden test file.");
    }
}
