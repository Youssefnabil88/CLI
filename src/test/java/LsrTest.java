import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class LsrTest {
    private static final String TEST_DIR = "testDir";
    private static final String SUB_DIR_1 = "subDir1";

    @BeforeEach
    public void setUp() throws IOException {
        Files.createDirectories(Paths.get(TEST_DIR));
        
    }

    @AfterEach
    public void cleanUp() throws IOException {
        Files.walk(Paths.get(TEST_DIR))
            .map(Path::toFile)
            .forEach(File::delete);
        Files.deleteIfExists(Paths.get(TEST_DIR));
    }

    @Test
    public void testListDirectoryRecursive() {
        CommandExecution.listDirectoryRecursive();
    }

    @Test
    public void testEmptyDirectory() throws IOException {
        String emptyDir = TEST_DIR + "/emptyDir";
        Files.createDirectory(Paths.get(emptyDir));

        CommandExecution.listDirectoryRecursive();

        Files.delete(Paths.get(emptyDir));
    }

    @Test
    public void testHiddenFiles() throws IOException {
        Files.createFile(Paths.get(TEST_DIR, ".hiddenFile.txt"));

        CommandExecution.listDirectoryRecursive();

    }

    
}
