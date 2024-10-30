import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os.CommandExecution;

class AbdelrahmanTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private static final String TEST_DIR_NAME = "testDir";
    private static final String EXISTING_DIR_NAME = "existingDir";
    private static final String TEST_FILE_NAME = "testFile.txt";

    @BeforeEach
    void setUp() throws Exception {
        System.setOut(new PrintStream(outputStreamCaptor));
        
        // إنشاء مجلدات للاختبار
        File existingDir = new File(EXISTING_DIR_NAME);
        if (!existingDir.exists()) {
            existingDir.mkdir();
        }

        // إنشاء ملف للاختبار
        Files.write(Paths.get(TEST_FILE_NAME), "Hello, World!".getBytes());
        
        // إنشاء مجلد للتأكد من أنه يمكن حذفه
        File testDir = new File(TEST_DIR_NAME);
        if (!testDir.exists()) {
            testDir.mkdir();
        }
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out); // إعادة تعيين System.out
        // حذف الملفات والمجلدات التي تم إنشاؤها
        File testDir = new File(TEST_DIR_NAME);
        if (testDir.exists()) {
            testDir.delete();
        }
        
        File existingDir = new File(EXISTING_DIR_NAME);
        if (existingDir.exists()) {
            existingDir.delete();
        }

        File testFile = new File(TEST_FILE_NAME);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    // اختبار rmdir
    @Test
    void testNoDirectoryNameProvidedForRmdir() {
        String[] commandArgs = {"rmdir"};
        CommandExecution.rmdir(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("!No file or directory name provided.", output);
    }

    @Test
    void testRemoveExistingDirectory() {
        String[] commandArgs = {"rmdir", TEST_DIR_NAME};
        CommandExecution.rmdir(commandArgs);
        File removedDir = new File(TEST_DIR_NAME);
        assertEquals(false, removedDir.exists()); // تأكد من أنه تم حذفه
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Directory removed: " + TEST_DIR_NAME, output);
    }

    @Test
    void testDirectoryNotEmptyForRmdir() {
        String[] commandArgs = {"rmdir", EXISTING_DIR_NAME};
        CommandExecution.rmdir(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Directory removed: " + EXISTING_DIR_NAME, output);
    }

    @Test
    void testDirectoryDoesNotExistForRmdir() {
        String[] commandArgs = {"rmdir", "nonExistentDir"};
        CommandExecution.rmdir(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("!No such file or directory: nonExistentDir", output);
    }

    // اختبار cat
    @Test
    void testCatFile() {
        String[] commandArgs = {"cat", TEST_FILE_NAME};
        CommandExecution.cat(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Hello, World!", output); // تحقق من محتويات الملف
    }

    @Test
    void testCatNonExistentFile() {
        String[] commandArgs = {"cat", "nonExistentFile.txt"};
        CommandExecution.cat(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals("Error: File does not exist.", output); // تحقق من الرسالة
    }

    // اختبار ls
    @Test
    void testLs() {
        String[] commandArgs = {"ls"};
        CommandExecution.ls(commandArgs);
        String output = outputStreamCaptor.toString().trim();
        assertEquals(TEST_DIR_NAME + "\n" + EXISTING_DIR_NAME, output); // تحقق من ظهور أسماء المجلدات
    }
}