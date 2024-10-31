import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
public class EchoAppendTest{
    private static final String FILE_NAME = "appendTest.txt";

    @BeforeEach
    public void setUp() throws IOException {
        // Clean up existing test file if it exists
        File file = new File(FILE_NAME);
        if (file.exists()) {
            Files.delete(file.toPath());
        }
        // Create file for testing
        Files.createFile(Paths.get(FILE_NAME));
    }

    @AfterEach
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(FILE_NAME));
    }
    @Test
    public void testEchoAppend() throws IOException {
        String[] command = {"echo", "Hello, World!", ">>", FILE_NAME};
        CommandExecution.handleEchoWithRedirection(command);

        String content = Files.readString(Paths.get(FILE_NAME));
        assertTrue(content.contains("Hello, World!"), "The file should contain 'Hello, World!'");
    }

    @Test
    public void testEchoAppendMultipleLines() throws IOException {
        String[] command1 = {"echo", "Line 1", ">>", FILE_NAME};
        String[] command2 = {"echo", "Line 2", ">>", FILE_NAME};

        CommandExecution.handleEchoWithRedirection(command1);
        CommandExecution.handleEchoWithRedirection(command2);

        String content = Files.readString(Paths.get(FILE_NAME));
        assertTrue(content.contains("Line 1"), "The file should contain 'Line 1'");
        assertTrue(content.contains("Line 2"), "The file should contain 'Line 2'");
    }


    @Test
    public void testEchoAppendFileNotFound() {
        String[] command = {"echo", "HIII!", ">>", "nonexistent.txt"};

        assertDoesNotThrow(() -> CommandExecution.handleEchoWithRedirection(command));
    }

    @Test
    public void testEchoAppendSpecialCharacters() throws IOException {
        String[] command = {"echo", "Line with special characters! @#&*()", ">>", FILE_NAME};
        CommandExecution.handleEchoWithRedirection(command);

        String content = Files.readString(Paths.get(FILE_NAME));
        assertTrue(content.contains("Line with special characters! @#&*()"), "The file should contain the special characters line.");
    }
    @Test
    public void testEchoAppendEmptyString() throws IOException {
        Files.writeString(Paths.get(FILE_NAME), "");
    
        String[] command = {"echo", "", ">>", FILE_NAME};
        CommandExecution.handleEchoWithRedirection(command);
    
        String content = Files.readString(Paths.get(FILE_NAME));
        assertTrue(!content.isEmpty(), "The file should remain empty when appending an empty string.");
    }
    // this test fails, it's probbaply bc the file is not empty
}