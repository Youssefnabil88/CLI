import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class CatCommandTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Path tempDirectory;

    @BeforeEach
    public void setUp() throws IOException {
        System.setOut(new PrintStream(outputStreamCaptor));

        // Create a temporary directory for test files
        tempDirectory = Files.createTempDirectory("testDir");
        System.setProperty("user.dir", tempDirectory.toString());
    }

    @AfterEach
    public void tearDown() throws IOException {
        System.setOut(originalOut);
        Files.walk(tempDirectory)
                .sorted((p1, p2) -> p2.compareTo(p1)) // Delete files first, then the directory
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Test
    public void testCatWriteToFile() throws IOException {
        String simulatedInput = "Hello!\nThis is a test.\n";
        InputStream input = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(input);

        String[] commandArgs = {"cat", ">", "testFile.txt"};
        CommandExecution.cat(commandArgs);

        assertTrue(new File(tempDirectory.toFile(), "testFile.txt").exists(), "File should be created");

        String content = Files.readString(Path.of(tempDirectory.toString(), "testFile.txt"));
        assertEquals("Hello!\nThis is a test.", content.trim());
    }

    @Test
    public void testCatReadFile() throws IOException {
        String testContent = "Hello!\nThis is a test.";
        Files.writeString(Path.of(tempDirectory.toString(), "testFile.txt"), testContent);

        String[] commandArgs = {"cat", "testFile.txt"};
        CommandExecution.cat(commandArgs);

        assertEquals("Hello!\nThis is a test.", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testCatFileNotFound() {
        String[] commandArgs = {"cat", "nonexistent.txt"};
        CommandExecution.cat(commandArgs);

        assertEquals("Error: File does not exist.", outputStreamCaptor.toString().trim());
    }

    @Test
    public void testCatNotAFile() throws IOException {
        Files.createDirectory(Path.of(tempDirectory.toString(), "not_a_file_dir"));

        String[] commandArgs = {"cat", "not_a_file_dir"};
        CommandExecution.cat(commandArgs);

        assertEquals("Error: Specified path is not a file.", outputStreamCaptor.toString().trim());
    }
}
