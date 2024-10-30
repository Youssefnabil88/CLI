import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;



class MkdirCommandTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static final String TEST_DIR_NAME = "testDir";
    private static final String EXISTING_DIR_NAME = "existingDir";

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        File existingDir = Paths.get(System.getProperty("user.dir"), EXISTING_DIR_NAME).toFile();
        if (!existingDir.exists()) {
            existingDir.mkdir();
        }
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
        File testDir = Paths.get(System.getProperty("user.dir"), TEST_DIR_NAME).toFile();
        if (testDir.exists()) {
            testDir.delete();
        }

        File existingDir = Paths.get(System.getProperty("user.dir"), EXISTING_DIR_NAME).toFile();
        if (existingDir.exists()) {
            existingDir.delete();
        }
    }

    @Test
    void testNoDirectoryNameProvided() {
        String[] commandArgs = {"mkdir"};
        CommandExecution.mkdir(commandArgs); 
        String output = outputStreamCaptor.toString().trim();
        assertEquals("!No directory name provided.", output);
    }

    @Test
    void testCreateNewDirectory() {
        String[] commandArgs = {"mkdir", TEST_DIR_NAME};
        CommandExecution.mkdir(commandArgs);  
        File createdDir = Paths.get(System.getProperty("user.dir"), TEST_DIR_NAME).toFile();
        assertEquals(true, createdDir.exists() && createdDir.isDirectory());
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Directory created: " + TEST_DIR_NAME, output);
    }

    @Test
    void testDirectoryAlreadyExists() {
        String[] commandArgs = {"mkdir", EXISTING_DIR_NAME};
        CommandExecution.mkdir(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("!Directory already exists: " + EXISTING_DIR_NAME, output);
    }



}
