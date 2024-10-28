package org.os;

import com.sun.tools.jconsole.JConsoleContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class CommandExecution {

    public static void execute(String[] commandArgs) {
        if (commandArgs.length == 0) {
            System.out.println("No command provided.");
            return;
        }

        switch (commandArgs[0]) {
            case "pwd":
                System.out.println(System.getProperty("user.dir"));
                break;
            case "touch":
                touch(commandArgs);
                break;
            case "exit":
                System.out.println("Exiting CLI.");
                System.exit(0);
            case "help":
                displayHelp();
                break;
                case "cd":
                cd(commandArgs);
                break;
            case "mkdir":
                mkdir(commandArgs);
                break;
            case "rm":
                rm(commandArgs);
                break;
            default:
                System.out.println("Unknown command: " + commandArgs[0]);
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

    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  pwd           - Print the current working directory.");
        System.out.println("  ls -a        - List the files in the current directory including hidden files.");
        System.out.println("  exit         - Exit the command line interface.");
        System.out.println("  help         - Display this help message.");
        System.out.println("  cd <path>     - Change the current directory to <path>.");
        System.out.println("  mkdir <name>  - Create a new directory with the specified <name>.");
        System.out.println("  rm <name>  - Remove directory OR File with the specified <name>.");
        System.out.println("  grep <pattern>    - Search for lines containing <pattern> from input.");
        System.out.println("  |                 - Pipe the output of one command to another (e.g., 'ls | grep txt').");
        System.out.println("  more              - View output one screen at a time (e.g., 'ls | more').");
        System.out.println("  less         - View output with scroll capability (e.g., 'ls | less').");
    }

    public static void touch(String[] commandArgs) {
        if (commandArgs.length < 2) {
            System.out.println("Error: No filename provided.");
            return;
        }

        String filename = commandArgs[1];
        Path filePath = Paths.get(System.getProperty("user.dir"), filename);
        File file = filePath.toFile();

        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("Error: File already exists: " + file.getName());
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }



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
            paginateOutput(output, 10);
        } else if ("less".equals(command2[0])) {
            paginateOutputWithScroll(output);
        } else {
            System.out.println("!Unknown command after pipe: " + command2[0]);
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





    // Method to execute a command and return its output as a String
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
            default:
                output.append("Unknown command: ").append(command[0]).append("\n");
        }
        return output.toString();
    }
}
