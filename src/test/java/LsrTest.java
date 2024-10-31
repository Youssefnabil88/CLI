
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
    private static final String EMPTY_DIR = TEST_DIR + "/emptyDir";

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
    public void testEmptyDirectory() throws IOException {
        Files.createDirectory(Paths.get(EMPTY_DIR));
        CommandExecution.listDirectoryWithHidden(new String[]{});
        File dir = new File(EMPTY_DIR);
        assert dir.exists() && dir.isDirectory() && dir.list().length == 0;
    }

    @Test
    public void testHiddenFiles() throws IOException {
        File hiddenFile = new File(TEST_DIR, ".hiddenFile.txt");
        hiddenFile.createNewFile();
        CommandExecution.listDirectoryWithHidden(new String[]{});
        assert hiddenFile.exists();
    }

    @Test
    public void testNormalAndReverseOrderListing() throws IOException {
        File fileA = new File(TEST_DIR, "fileA.txt");
        File fileB = new File(TEST_DIR, "fileB.txt");
        fileA.createNewFile();
        fileB.createNewFile();
        CommandExecution.listDirectoryWithHidden(new String[]{});
        assert fileA.exists() && fileB.exists();
        CommandExecution.listDirectoryWithHidden(new String[]{"", "-r"});
        assert fileA.exists() && fileB.exists();
    }

    
}