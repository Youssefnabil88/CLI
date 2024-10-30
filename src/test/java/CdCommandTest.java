import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;


class CdCommandTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream(); // Captures console output
    private static final String TEST_DIR_NAME = "testDir";
    private static final String TEST_PARENT_DIR = "..";

    @BeforeEach
    void setUp() {
     
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
        File testDir = Paths.get(System.getProperty("user.dir"), TEST_DIR_NAME).toFile();
        if (testDir.exists()) {
            testDir.delete();
        }
    }

    @Test
    void testNoPathProvided() {
        String[] commandArgs = {"cd"};
        CommandExecution.cd(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Error: No path provided.", output);
    }

    @Test
    void testParentDirectoryNavigation() {
        String initialDir = System.getProperty("user.dir");
        String[] commandArgs = {"cd", TEST_PARENT_DIR};
        
        CommandExecution.cd(commandArgs);

        String expectedDir = Paths.get(initialDir).getParent() != null ?
                             Paths.get(initialDir).getParent().toString() : initialDir;
                             
        assertEquals(expectedDir, System.getProperty("user.dir"));
    }

    @Test
    void testValidDirectoryNavigation() {
        String initialDir = System.getProperty("user.dir");
        File testDir = new File(initialDir, TEST_DIR_NAME);

        if (!testDir.exists()) testDir.mkdir();

        String[] commandArgs = {"cd", TEST_DIR_NAME};
        CommandExecution.cd(commandArgs);

        assertEquals(testDir.getAbsolutePath(), System.getProperty("user.dir"));

        testDir.delete();
    }

    @Test
    void testInvalidDirectoryNavigation() {
        String[] commandArgs = {"cd", "nonExistentDir"};
        CommandExecution.cd(commandArgs);

        String output = outputStreamCaptor.toString().trim();
        assertEquals("Error: Directory does not exist.", output);
    }
}