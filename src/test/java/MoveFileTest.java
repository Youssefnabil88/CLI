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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MoveFileTest {
    private static final String INITIAL_PATH = "/home/youssef/Desktop";
    private static final String SOURCE_PATH1 = "new";
    private static final String SOURCE_PATH2 = "newfile";
    private static final String SOURCE_PATH3 = "newtestfile";
    private static final String DESTINATION_PATH = "mm";
    private static final String DESTINATION_DIR_PATH = "newdestinationDir";

    
    @BeforeEach
    public void setUp() {
        System.setProperty("user.dir", INITIAL_PATH);
    }


    @Test
    void testMoveSingleFileRename() throws IOException {
        CommandExecution.moveFiles(List.of(SOURCE_PATH1, DESTINATION_DIR_PATH), false);

        assertFalse(Files.exists(Paths.get(SOURCE_PATH1)), "Source file should no longer exist");
    }

    @Test
    void testErrorForNonexistentSource() {
        Path nonexistentSource = Paths.get("tttttt.txt");
        Path destination = Paths.get(DESTINATION_PATH);

        assertFalse(Files.exists(nonexistentSource), "Source file should not exist");

        CommandExecution.moveFiles(List.of(nonexistentSource.toString(), destination.toString()), false);

        assertFalse(Files.exists(nonexistentSource), "Source file should still not exist");
        assertFalse(Files.exists(destination), "Destination file should not be created or altered");
    }

    @Test
    void testMoveMultipleFilesToDirectory() throws IOException {
        Path destinationPath=Paths.get(DESTINATION_PATH);
        CommandExecution.moveFiles(List.of(SOURCE_PATH1, SOURCE_PATH2, DESTINATION_PATH), false);

        assertFalse(Files.exists(Paths.get(SOURCE_PATH1)), "Source file1 should no longer exist");
        assertFalse(Files.exists(Paths.get(SOURCE_PATH2)), "Source file2 should no longer exist");

        //assertTrue(Files.exists(destinationPath.resolve(SOURCE_PATH1)), "file1 should exist in the destination directory");
        //assertTrue(Files.exists(destinationPath.resolve(SOURCE_PATH2)), "file2 should exist in the destination directory");
    }

}