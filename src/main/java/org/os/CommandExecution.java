package org.os;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;

public class CommandExecution {
    public static boolean isTestEnvironment = false; // Flag for testing env.

    public static void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("No command provided.");
            return;
        }

        switch (args[0]) {
            case "pwd":
                System.out.println(System.getProperty("user.dir"));
                break;
            case "touch":
                touch(args);
                break;
            case "ls":
                ls(args);
                break;
            case "exit":
                System.out.println("Exiting CLI.");
                System.exit(0);
            case "help":
                displayHelp();
                break;
            case "cd":
                cd(args);
                break;
            case "mkdir":
                mkdir(args);
                break; 
            case "rmdir":
                rmdir(args);
            break;
            case "echo":
                 handleEchoWithRedirection(args);
            break; 
            case "cat":
                cat(args);
                break;
            case "rm":
                rm(args);
                break;
                case "mv":
                if (args.length < 3) {
                    System.out.println("Error: At least a source and destination path are required.");
                } else {
                    // Convert the args array to a list, starting from index 1 to include paths only
                    List<String> paths = Arrays.asList(args);
                    moveFiles(paths, false);
                }
                break;
            
            default:
                System.out.println("Unknown command: " + args[0]);
        }
    }

                                    /* Youssef */

    // Method to write output to a file, overwriting existing content (>)
    public static void writeToFile(String[] command, String fileName) {
        try {

            File file = new File(System.getProperty("user.dir"), fileName);
            try (FileWriter writer = new FileWriter(file)) {
                String output = CommandExecution.executeCommandAndGetOutput(command);
                writer.write(output);
                writer.write(System.lineSeparator()); // Add a newline
                System.out.println("Output written to " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void listDirectoryWithHidden(String[] commandArgs) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        File directory = currentPath.toFile();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            } else {
                System.out.println("The directory is empty.");
            }
        } else {
            System.out.println("Error: No such directory: " + currentPath);
        }
    }

    public static void touch(String[] commandArgs) {
        if (commandArgs.length < 2) {
            System.out.println("Error: No filenames provided.");
            return;
        }
    
        for (int i = 1; i < commandArgs.length; i++) {
            String filename = commandArgs[i];
            Path filePath = Paths.get(System.getProperty("user.dir"), filename);
            File file = filePath.toFile();
    
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getName());
                } else {
                    System.out.println("File already exists: " + file.getName());
                }
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage() + " for " + filename);
            }
        }
    }

    // Method to execute a command and return its output as a String
    // Used in write to file & Pipe |
    public static String executeCommandAndGetOutput(String[] command) {
        
        StringBuilder output = new StringBuilder();
        switch (command[0]) {
            case "ls":
                File currentDirectory = new File(System.getProperty("user.dir"));
                File[] files = currentDirectory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        output.append(file.getName()).append("\n");
                    }
                }
                break;
            case "pwd":
                output.append(System.getProperty("user.dir")).append("\n");
                break;

                case "cd":
                cd(command);
                break;

            case "mkdir":
                mkdir(command);
                break;

            case "rm":
            rm(command);
            break;
            case "mv":
            //  mv(command);
             break;
             case "echo":
             handleEchoWithRedirection(command);
              break;
            default:
                output.append("Unknown command: ").append(command[0]).append("\n");
        }
        return output.toString();
    }


    /*======================================================================================================= */
    
                                     /* Abdelrahman */

    //Duaa Added ls -r functionality to this function.
    public static void ls(String[] commandArgs) {
    Path currentPath = Paths.get(System.getProperty("user.dir"));
    File directory = currentPath.toFile();

    if (directory.exists() && directory.isDirectory()) {
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            // Reverse listing if "-r" argument is provided
            if (commandArgs.length > 1 && "-r".equals(commandArgs[1])) {
                Arrays.sort(files, (a, b) -> b.getName().compareTo(a.getName()));
            } else {
                Arrays.sort(files, (a, b) -> a.getName().compareTo(b.getName()));
            }

            for (File file : files) {
                if (!file.isHidden()) {
                    System.out.println(file.getName());
                }
            }
        } else {
            System.out.println("The directory is empty.");
        }
    } else {
        System.out.println("Error: No such directory: " + currentPath);
    }
}
                                    
    
    // Method to display the contents of a file
    public static void cat(String[] commandArgs) {


        if (commandArgs.length < 2) {
            System.out.println("Error: No filename provided.");
            return;
        }

        String filename = commandArgs[1];
        File file = new File(System.getProperty("user.dir"), filename);

        if (!file.exists()) {
            System.out.println("Error: File does not exist.");
            return;
        }

        if (!file.isFile()) {
            System.out.println("Error: Specified path is not a file.");
            return;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                System.out.println(fileScanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    } 

    public static void rmdir(String[] commandArgs) {
        if (commandArgs.length < 2) {
            System.out.println("!No file or directory name provided.");
            return;
        }
    
        for (int i = 1; i < commandArgs.length; i++) {
            String target = commandArgs[i];
            File targetFile = new File(System.getProperty("user.dir"), target);
    
            if (!targetFile.exists()) {
                System.out.println("!No such file or directory: " + target);
                continue;
            }
    
            if (targetFile.isDirectory()) {
                // Check if the directory is empty
                if (targetFile.list().length == 0) {
                    if (targetFile.delete()) {
                        System.out.println("Directory removed: " + target);
                    } else {
                        System.out.println("!Failed to remove directory (insufficient permissions): " + target);
                    }
                } else {
                    System.out.println("!Directory is not empty: " + target);
                }
            } else {
                System.out.println("!The specified target is not a directory: " + target);
            }
        }
    }

    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  pwd               - Print the current working directory.");
        System.out.println("  ls -a             - List the files in the current directory including hidden files.");
        System.out.println("  ls -r             - List the files in the current directory in reverse way.");        
        System.out.println("  exit              - Exit the command line interface.");
        System.out.println("  help              - Display this help message.");
        System.out.println("  cd <path>         - Change the current directory to <path>.");
        System.out.println("  mkdir <name>      - Create a new directory with the specified <name>.");
        System.out.println("  rm <name>         - Remove directory OR File with the specified <name>.");
        System.out.println("  mv <source file/s> <destination>     - move one or more files to destination directory.");
        System.out.println("  grep <pattern>    - Search for lines containing <pattern> from input.");
        System.out.println("  |                 - Pipe the output of one command to another (e.g., 'ls | grep txt').");
        System.out.println("  more              - View output one screen at a time (e.g., 'ls | more').");
        System.out.println("  less              - View output with scroll capability (e.g., 'ls | less').");
    }
    /*======================================================================================================= */


                                        /* Helana */
    public static void cd(String[] commandArgs) {
        if (commandArgs.length < 2) {
            System.out.println("Error: No path provided.");
            return;
        }

        String path = commandArgs[1];
        if ("..".equals(path)) {
            Path parentPath = Paths.get(System.getProperty("user.dir")).getParent();
            if (parentPath != null) {
                System.setProperty("user.dir", parentPath.toAbsolutePath().toString());
            } else {
                System.out.println("Error: Already at the root directory.");
            }
            return;
        }

        Path newPath = Paths.get(System.getProperty("user.dir"), path).normalize();
        File directory = newPath.toFile();

        if (directory.exists() && directory.isDirectory()) {
            System.setProperty("user.dir", directory.getAbsolutePath());
        } else {
            System.out.println("Error: Directory does not exist.");
        }
    }

    public static void mkdir(String[] commandArgs) {
        if (commandArgs.length < 2) {
            System.out.println("!No directory name provided.");
            return;
        }
    
        for (int i = 1; i < commandArgs.length; i++) {
            String dirName = commandArgs[i];
            Path dirPath = Paths.get(System.getProperty("user.dir"), dirName);
            File directory = dirPath.toFile();
    
            if (directory.exists()) {
                System.out.println("!Directory already exists: " + dirName);
            } else {
                if (directory.mkdir()) {
                    System.out.println("Directory created: " + dirName);
                } else {
                    System.out.println("!Failed to create directory: " + dirName);
                }
            }
        }
    }
    
    public static void rm(String[] commandArgs) {
        if (commandArgs.length < 2) {
            System.out.println("!No file or directory name provided.");
            return;
        }

        for (int i = 1; i < commandArgs.length; i++) {
            String Removed = commandArgs[i];
            File DirOrFile = new File(System.getProperty("user.dir"), Removed);

            if (!DirOrFile.exists()) {
                System.out.println("!No such file or directory: " + Removed);
                continue;
            }

            if (DirOrFile.isDirectory()) {
                
                if (deleteDirectory(DirOrFile)) {
                    System.out.println("Directory removed: " + Removed);
                } else {
                    System.out.println("!Failed to remove directory: " + Removed);
                }
            } else {
                if (DirOrFile.delete()) {
                    System.out.println(Removed+" File removed successfully!");
                } else {
                    System.out.println("!Failed to remove your provided file name: " + Removed);
                }
            }
        }
    }

    public static void PipeCommand(String[] commandArgs) {
        int pipeIndex = -1;
        for (int i = 0; i < commandArgs.length; i++) {
            if ("|".equals(commandArgs[i])) {
                pipeIndex = i;
                break;
            }
        }
    
        if (pipeIndex == -1) {
            execute(commandArgs);
            return;
        }
    
        String[] command1 = Arrays.copyOfRange(commandArgs, 0, pipeIndex);
        String[] command2 = Arrays.copyOfRange(commandArgs, pipeIndex + 1, commandArgs.length);
        String output = executeCommandAndGetOutput(command1);
    
        if ("grep".equals(command2[0])) {
            String pattern = command2.length > 1 ? command2[1] : "";
            for (String line : output.split("\n")) {
                if (line.contains(pattern)) {
                    System.out.println(line);
                }
            }
        } else if ("more".equals(command2[0])) {
            // Simulate more output for testing purposes
            if (isTestEnvironment) {
                simulateMoreOutput(output);
            } else {
                paginateOutput(output, 10);
            }
        } else if ("less".equals(command2[0])) {
            if (isTestEnvironment) {
             testPaginateOutputWithScroll(output);
            }
            else
                paginateOutputWithScroll(output);
        } else {
            System.out.println("!Unknown command after pipe: " + command2[0]);
        }
    }
    // New method to simulate the behavior of more
    private static void simulateMoreOutput(String output) {
        String[] lines = output.split("\n");
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
            // Simulate "Press Enter to continue..." after every 10 lines
            if ((i + 1) % 10 == 0) {
                System.out.println("Press Enter to continue...");
                // Here you can add a break if testing or a wait mechanism in real usage
            }
        }
    }

    private static void testPaginateOutputWithScroll(String output) {
        String[] lines = output.split("\n");
        int currentLine = 0;
        int totalLines = lines.length;
        int scrollDownSteps = 0; // Number of times to scroll down (for simulation)
    
        while (currentLine < totalLines) {
            // Print the next 10 lines or remaining lines
            for (int i = 0; i < 10 && currentLine < totalLines; i++, currentLine++) {
                System.out.println(lines[currentLine]);
            }
    
            // Check if there are more lines to display
            if (currentLine < totalLines) {
                // Simulate user input for scrolling behavior
                if (scrollDownSteps < 3) { // Simulating pressing 'f' three times
                    System.out.println("Simulated input: 'f' (scroll down)");
                    scrollDownSteps++;
                } else {
                    // You can simulate pressing 'q' to quit for the sake of testing
                    System.out.println("Simulated input: 'q' (quit)");
                    break; // Exit the loop to stop the simulation
                }
            }
        }
    }

    private static void paginateOutput(String output, int linesPerPage) {
        String[] lines = output.split("\n");
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
            if ((i + 1) % linesPerPage == 0 && i != lines.length - 1) {
                System.out.println("Press Enter to continue...");
                new Scanner(System.in).nextLine();
            }
        }
    }

    private static void paginateOutputWithScroll(String output) {
        String[] lines = output.split("\n");
        int currentLine = 0;
        Scanner scanner = new Scanner(System.in);

        while (currentLine < lines.length) {
            for (int i = 0; i < 10 && currentLine < lines.length; i++, currentLine++) {
                System.out.println(lines[currentLine]);
            }

            if (currentLine < lines.length) {
                System.out.println("Press 'f' to scroll down, 'b' to scroll up, or 'q' to quit...");
                String input = scanner.nextLine().toLowerCase();

                if ("q".equals(input)) {
                    break;
                } else if ("b".equals(input) && currentLine >= 20) {
                    currentLine -= 20;
                } else if ("f".equals(input)) {
                    continue;
                } else {
                    System.out.println("Invalid input, please use 'f', 'b', or 'q'.");
                    currentLine -= 10;
                }
            }
        }
    }

    //Helber fn Used in rm
    public static boolean deleteDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                }
                file.delete();
            }
        }
        return dir.delete();
    }
  

    /*======================================================================================================= */
    
                                        /* Duaa */

    public static void moveFiles(List<String> paths, boolean force) {
        if (paths.size() < 2) {
            System.out.println("Error: At least a source and destination path are required.");
            return;
        }

        String lastPath = paths.get(paths.size() - 1);
        File destination = new File(System.getProperty("user.dir"), lastPath);

        if (paths.size() == 2 && !destination.isDirectory()) {
            File source = new File(System.getProperty("user.dir"), paths.get(0));


            if (destination.exists() && !destination.canWrite() && !force && System.console() != null) {
                System.out.print("Overwrite " + destination.getName() + "? (y/N): ");
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine();
                if (!response.toLowerCase().startsWith("y")) {
                    System.out.println("Skipped: " + source.getName());
                    return;
                }
            }

            if (source.renameTo(destination)) {
                System.out.println("File renamed successfully to " + destination.getPath());
            } else {
                System.out.println("Error: Could not rename file.");
            }
            return;
        }

        if (paths.size() > 2 && !destination.isDirectory()) {
            System.out.println("Error: When moving multiple files, the destination must be a directory.");
            return;
        }

        for (int i = 1; i < paths.size() - 1; i++) {
            File source = new File(System.getProperty("user.dir"), paths.get(i));
            
            if (!source.exists()) {
                System.out.println("Error: Source file does not exist - " + source.getPath());
                continue;
            }

            File targetDestination = destination.isDirectory() ? new File(destination, source.getName()) : destination;

            if (targetDestination.exists() && !targetDestination.canWrite() && !force && System.console() != null) {
                System.out.print("Overwrite " + targetDestination.getName() + "? (y/N): ");
                Scanner scanner = new Scanner(System.in);
                String response = scanner.nextLine();
                if (!response.toLowerCase().startsWith("y")) {
                    System.out.println("Skipped: " + source.getName());
                    continue;
                }
            }

            try {
                Files.move(source.toPath(), targetDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved: " + source.getName() + " to " + targetDestination.getPath());
            } catch (IOException e) {
                System.out.println("Error: Could not move file " + source.getPath() + " due to I/O error.");
            }
        }
    }
    


    public static void handleEchoWithRedirection(String[] commandArgs) {
        StringBuilder contentToWrite = new StringBuilder();
        boolean append = false;
        String fileName = null;
    
        for (int i = 1; i < commandArgs.length; i++) {
            if (">".equals(commandArgs[i]) && i + 1 < commandArgs.length) {
                fileName = commandArgs[i + 1];
                append = false; // Overwrite mode;
                break;
            } else if (">>".equals(commandArgs[i]) && i + 1 < commandArgs.length) {
                fileName = commandArgs[i + 1];
                append = true; // Append mode
                break;
            }
    
            contentToWrite.append(commandArgs[i].isEmpty() ? "\n" : commandArgs[i] + " ");
        }
    
        if (fileName != null) {
            writeToFile(contentToWrite.toString().trim(), fileName, append);
        } else {
        
            System.out.println("Error: Redirection operator not followed by filename.");
        }
    }

    //Over ride WriteToFile fn() to match >> (append operation).
    public static void writeToFile(String content, String fileName, boolean append) {
        try {
            File file = new File(System.getProperty("user.dir"), fileName);
            
            try (FileWriter writer = new FileWriter(file, append)) {
                writer.write(content);
                writer.write(System.lineSeparator()); // Add a newline at the end of the content
                System.out.println("Output written to " + fileName + (append ? " (appended)" : " (overwritten)"));
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
