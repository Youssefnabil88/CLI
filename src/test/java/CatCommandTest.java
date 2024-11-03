import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class CatCommandTest {
    private final String testDir = System.getProperty("user.dir") + "/testDir";
    private final String inputFilePath = testDir + "/file1.txt";
    private final String outputFilePath = testDir + "/file2.txt";
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        new File(testDir).mkdirs();
        createTestFile(inputFilePath, "Hello, World!\n");
    }

    @AfterEach
    void tearDown() {

        new File(inputFilePath).delete();
        new File(outputFilePath).delete();
        new File(testDir).delete();
        System.setOut(originalOut);
    }

    private void createTestFile(String path, String content) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(content);
        } catch (IOException e) {
            fail("Failed to create test file: " + e.getMessage());
        }
    }

    @Test
    void testReadFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CommandExecution.cat(new String[]{"cat", "testDir/file1.txt"});
        assertEquals("Hello, World!\n", outContent.toString());
    }

    @Test
    void testRedirectOutput() {

        CommandExecution.cat(new String[]{"cat", "testDir/file1.txt", ">", "testDir/file2.txt"});
        File file = new File(outputFilePath);
        assertTrue(file.exists(), "Output file was not created.");

        String content = readFile(outputFilePath);
        assertEquals("Hello, World!\n", content);
    }

    @Test
    void testFileNotFound() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        CommandExecution.cat(new String[]{"cat", "testDir/non_existent_file.txt"});
        assertTrue(outContent.toString().contains("Error: File does not exist:"));
    }

    private String readFile(String path) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(new File(path))) {
            while (scanner.hasNextLine()) {
                contentBuilder.append(scanner.nextLine()).append("\n");
            }
        } catch (IOException e) {
            fail("Failed to read file: " + e.getMessage());
        }
        return contentBuilder.toString();
    }
}
