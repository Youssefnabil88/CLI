import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class MoveFileTest {
    private static final String INITIAL_PATH = "/home/youssef/Desktop";
    private static final String SOURCE_PATH = "test.txt";
    private static final String DESTINATION_PATH = "mm";

    @BeforeEach
    public void setUp() {
        System.setProperty("user.dir", INITIAL_PATH);
    }

    @AfterEach
    public void cleanUp() throws IOException {
        Files.deleteIfExists(Paths.get(SOURCE_PATH));
        Files.deleteIfExists(Paths.get(DESTINATION_PATH));
    }

    @Test
    public void testMoveFileSuccessful() throws IOException {
        Files.writeString(Paths.get(SOURCE_PATH), "some content");

        CommandExecution.moveFiles(SOURCE_PATH, DESTINATION_PATH);
        
        if(Paths.get(DESTINATION_PATH)!=null)//mean that the destinationPath is a directory then should move the source file into the destination, then the sourcePath should be still there
            assertTrue(Files.exists(Paths.get(SOURCE_PATH)), "Source file should exist after moveing it into the destination path.");
        else
            assertTrue(Files.exists(Paths.get(DESTINATION_PATH)), "Destination file should exist after move.");
    }

    @Test
    public void testMoveFile_sourceAndDestinationAreSame() throws IOException {
        String sourcePath = "testFile.txt";
        String destinationPath = "testFile.txt";

        File source = new File(sourcePath);
        source.createNewFile();

        CommandExecution.moveFile(sourcePath, destinationPath);

        assertTrue(source.exists(), "File should not be affected as source and destination are the same");

        source.delete();
    }


}