import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;


class RmCommandTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static final String TEST_FILE_NAME = "testFile.txt";
    private static final String TEST_DIR_NAME = "testDir";
    private static final String NESTED_DIR_NAME = "nestedDir";
    private static final String NESTED_FILE_NAME = "nestedFile.txt";

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        try {
            new File(TEST_FILE_NAME).createNewFile();
            File testDir = new File(TEST_DIR_NAME);
            testDir.mkdir();
            File nestedDir = new File(testDir, NESTED_DIR_NAME);
            nestedDir.mkdir();
            new File(nestedDir, NESTED_FILE_NAME).createNewFile();
        } catch (Exception e) {
            fail("Failed to create test setup: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
        new File(TEST_FILE_NAME).delete();
        
        File nestedFile = new File(TEST_DIR_NAME, NESTED_DIR_NAME + File.separator + NESTED_FILE_NAME);
        File nestedDir = new File(TEST_DIR_NAME, NESTED_DIR_NAME);
        
        if (nestedFile.exists()) {
            nestedFile.delete();
        }
 
        if (nestedDir.exists()) {
            nestedDir.delete();
        }
      
        File testDir = new File(TEST_DIR_NAME);
        if (testDir.exists()) {
            testDir.delete();
        }
    }

    @Test
    void testNoArgumentsProvided() {
        String[] commandArgs = {"rm"};
        CommandExecution.rm(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("!No file or directory name provided.", output);
    }

    @Test
    void testFileRemoval() {
        String[] commandArgs = {"rm", TEST_FILE_NAME};
        CommandExecution.rm(commandArgs);
        
        File removedFile = new File(TEST_FILE_NAME);
        String output = outputStreamCaptor.toString().trim();
        assertEquals(TEST_FILE_NAME + " File removed successfully!", output);
        assertEquals(false, removedFile.exists());
    }

    @Test
    void testDirectoryRemoval() {
        String[] commandArgs = {"rm", TEST_DIR_NAME};
        CommandExecution.rm(commandArgs);
        
        File removedDir = new File(TEST_DIR_NAME);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Directory removed: " + TEST_DIR_NAME, output);
        assertEquals(false, removedDir.exists());
    }

    @Test
    void testRemoveNonExistentFile() {
        String[] commandArgs = {"rm", "nonExistentFile.txt"};
        CommandExecution.rm(commandArgs);
        
        String output = outputStreamCaptor.toString().trim();
        assertEquals("!No such file or directory: nonExistentFile.txt", output);
    }
    
    @Test
    void testRemoveNonExistentDirectory() {
        String[] commandArgs = {"rm", "nonExistentDir"};
        CommandExecution.rm(commandArgs);
        
        String output = outputStreamCaptor.toString().trim();
        assertEquals("!No such file or directory: nonExistentDir", output);
    }

    @Test
    void testRemoveDirectoryWithNestedFiles() {
        String[] commandArgs = {"rm", TEST_DIR_NAME};
        CommandExecution.rm(commandArgs);
        
        File removedDir = new File(TEST_DIR_NAME);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Directory removed: " + TEST_DIR_NAME, output);
        assertEquals(false, removedDir.exists());
    }
}