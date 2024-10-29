import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import org.os.CommandExecution;

import static org.junit.jupiter.api.Assertions.*;

class TouchTest {

    private static final String TEST_FILENAME = "file.txt";

    @AfterEach
    void tearDown() {
        // Delete the file after each test if it exists
        File file = Paths.get(System.getProperty("user.dir"), TEST_FILENAME).toFile();
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testCreateNewFile() {
        String[] commandArgs = {"touch", TEST_FILENAME};

        CommandExecution.touch(commandArgs);

        // Assert that the file was created
        File file = Paths.get(System.getProperty("user.dir"), TEST_FILENAME).toFile();
        assertTrue(file.exists(), "File should  be created");
    }

    @Test
    void testCreateExistingFile() {
        // Create the file first
        File file = Paths.get(System.getProperty("user.dir"), TEST_FILENAME).toFile();
        try {
            assertTrue(file.createNewFile(), "File should be created initially");
        } catch (Exception e) {
            fail("Setup failed to create initial file: " + e.getMessage());
        }

        // Simulate the command arguments
        String[] commandArgs = {"touch", TEST_FILENAME};

        CommandExecution.touch(commandArgs);
        
        assertTrue(file.exists(), "File should still exist");
    }
}
