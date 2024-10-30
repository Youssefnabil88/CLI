import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;

class PipeCommandTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void testLsPipeGrepTxt() {
        String[] commandArgs = {"ls", "|", "grep", ".txt"};
        CommandExecution.PipeCommand(commandArgs); 

        String output = outputStream.toString().trim();
        assertTrue(output.contains("a.txt"));
        assertTrue(output.contains("b.txt"));
        assertTrue(output.contains("example.txt"));
        assertTrue(output.contains("file1.txt"));
        assertTrue(output.contains("hell.txt"));
    }

    @Test
    void testLsPipeMore() {
        String[] commandArgs = {"ls", "|", "more"};
        CommandExecution.PipeCommand(commandArgs);

        String output = outputStream.toString().trim();
        assertTrue(output.contains("a.txt"));
        assertTrue(output.contains("b.txt"));
        assertTrue(output.contains("example.txt"));
        assertTrue(output.contains("file1.txt"));
        assertTrue(output.contains("hell.txt"));
    }

    @Test
    void testLsPipeLess() {
        String[] commandArgs = {"ls", "|", "less"};
        CommandExecution.PipeCommand(commandArgs); 

        String output = outputStream.toString().trim();
        assertTrue(output.contains("a.txt"));
        assertTrue(output.contains("b.txt"));
        assertTrue(output.contains("example.txt"));
        assertTrue(output.contains("file1.txt"));
        assertTrue(output.contains("hell.txt"));
    }

    
    
}

  

