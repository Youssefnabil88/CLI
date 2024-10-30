import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.os.CommandExecution;

import static org.junit.jupiter.api.Assertions.*;

public class TestPipe {
    private CommandExecution command;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        command = new CommandExecution();
        System.setProperty("user.dir", "/home/youssef/Desktop/Courses/Test");
    }


    @Test
void testLsPipeGrepTxt() {
    String[] commandArgs = {"ls", "|", "grep", "file19"};
    
    // Redirect output to the outputStream
    System.setOut(new PrintStream(outputStream));
    
    CommandExecution.isTestEnvironment = true; 
    command.PipeCommand(commandArgs); 

    String output = outputStream.toString().trim();
    assertTrue(output.contains("file19.txt"), "Output should contain file19.txt");
    
    // Reset the output
    System.setOut(System.out);
}


    @Test
    public void testPipeCommandWithMore() {
        String[] commandArgs = {"ls", "|", "more"};
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
      
        CommandExecution.isTestEnvironment = true; 
        command.PipeCommand(commandArgs);
        
        System.setOut(System.out);
        System.out.println();
    
        String output = outContent.toString();
        assertTrue(output.contains("file10.txt"), "Output should contain file1.txt");
        assertTrue(output.contains("Press Enter to continue..."), "Output should contain the pagination prompt");
    }
    
    @Test
    public void testPipeCommandWithLess() {
        String[] commandArgs = {"ls", "|", "less"};
        
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        // Set test environment flag
        CommandExecution.isTestEnvironment = true; 
        command.PipeCommand(commandArgs);
        
        System.setOut(System.out);
        
        String output = outContent.toString();
        assertTrue(output.contains("file1.txt"), "Output should contain file1.txt");
        assertTrue(output.contains("Simulated input: 'f' (scroll down)"), "Output should contain the pagination prompt");
    }
     
}
