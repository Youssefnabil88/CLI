import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import org.os.CommandExecution;

public class TestOutputRedirection {
    private CommandExecution command;
    private final String testFileName = "testOutput.txt";
    private File outputFile;

    @BeforeEach
    public void setUp() {
        command = new CommandExecution();
        outputFile = new File(System.getProperty("user.dir"), testFileName);
        outputFile.deleteOnExit();  // Ensure the file is deleted after the test
    }

    @Test
    public void testWriteToFile() {
        String[] commandArgs = {"ls"};  // Command to execute

        // Call writeToFile method
        CommandExecution.writeToFile(commandArgs, testFileName);

        // Check file creation and content
        assertTrue(outputFile.exists(), "The output file should exist.");
        assertTrue(outputFile.length() > 0, "The output file should not be empty.");

        // Verify the contents of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
            String line;
            boolean containsPomXml = false;
            boolean containsSrc = false;
            boolean containsTarget = false;

            // Check each line for expected entries
            while ((line = reader.readLine()) != null) {
                if (line.trim().equals("pom.xml")) containsPomXml = true;
                if (line.trim().equals("src")) containsSrc = true;
                if (line.trim().equals("target")) containsTarget = true;
            }

            assertTrue(containsPomXml, "The output should contain 'pom.xml'.");
            assertTrue(containsSrc, "The output should contain 'src'.");
            assertTrue(containsTarget, "The output should contain 'target'.");
        } catch (IOException e) {
            fail("Failed to read the output file: " + e.getMessage());
        }
    }
}
