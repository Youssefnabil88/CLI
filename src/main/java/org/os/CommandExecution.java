package org.os;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class CommandExecution {
    
    public static void execute(String[] commandArgs) {
        if (commandArgs.length == 0) {
            System.out.println("No command provided.");
            return;
        }

        switch (commandArgs[0]) {
            case "pwd" -> System.out.println(System.getProperty("user.dir"));
            case "touch" -> touch(commandArgs);
            case "mv" -> {
                if (commandArgs.length < 3) {
                    System.out.println("Error: 'mv' requires source and destination paths.");
                } else {
                    moveFile(commandArgs[1], commandArgs[2]);
                }
            }
            case "echo" -> handleEchoWithRedirection(commandArgs);
            case "exit" -> {
                System.out.println("Exiting CLI.");
                System.exit(0);
            }
            case "help" -> displayHelp();
            case "ls" -> handleListDirectory(commandArgs);
            default -> System.out.println("Unknown command: " + commandArgs[0]);
        }
    }

    public static void handleEchoWithRedirection(String[] commandArgs) {
        StringBuilder contentToWrite = new StringBuilder();
        boolean append = false;
        String fileName = null;

        for (int i = 1; i < commandArgs.length; i++) {
            if (">".equals(commandArgs[i]) && i + 1 < commandArgs.length) {
                fileName = commandArgs[i + 1];
                append = false; // Overwrite
                break;
            } else if (">>".equals(commandArgs[i]) && i + 1 < commandArgs.length) {
                fileName = commandArgs[i + 1];
                append = true; // Append
                break;
            }
            contentToWrite.append(commandArgs[i]).append(" ");
        }

        if (fileName != null) {
            writeToFile(contentToWrite.toString().trim(), fileName, append);
        } else {
            System.out.println("Error: Redirection operator not followed by filename.");
        }
    }
    public static void moveFile(String sourcePath, String destinationPath) {
        File source = new File(System.getProperty("user.dir"), sourcePath);
        File destination = new File(System.getProperty("user.dir"), destinationPath);

        if (!source.exists()) {
            System.out.println("Error: Source file does not exist.");
            return;
        }

        if (destination.isDirectory()) {
            destination = new File(destination, source.getName());
        }

        if (source.renameTo(destination)) {
            System.out.println("File renamed successfully.");
        } else {
            try {
                Files.move(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("File moved successfully.");
            } catch (IOException e) {
                System.out.println("Error: Could not move file due to I/O error.");
            }
        }
    }

    public static void writeToFile(String content, String fileName, boolean append) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append))) {
            writer.write(content);
            writer.newLine(); // Add a newline after writing
            System.out.println("Output " + (append ? "appended to" : "written to") + " " + fileName);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void handleListDirectory(String[] commandArgs) {
        if (commandArgs.length > 1 && "-r".equals(commandArgs[1])) {
            listDirectoryRecursive();
        } else if (commandArgs.length > 1 && "-a".equals(commandArgs[1])) {
            listDirectoryWithHidden();
        } else {
            listDirectory();
        }
    }

    public static void listDirectory() {
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

    public static void listDirectoryWithHidden() {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        File directory = currentPath.toFile();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    System.out.println(file.getName());
                }
            } else {
                System.out.println("Error: The directory is empty or cannot be read.");
            }
        }
    }

    public static void listDirectoryRecursive() {
        File currentDir = new File(System.getProperty("user.dir"));
        listFilesRecursively(currentDir);
    }

    private static void listFilesRecursively(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                System.out.println(file.getAbsolutePath());
                if (file.isDirectory()) {
                    listFilesRecursively(file);
                }
            }
        }
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

    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  pwd           - Print the current working directory.");
        System.out.println("  ls            - List files in the current directory.");
        System.out.println("  ls -a         - List all files including hidden files.");
        System.out.println("  ls -r         - List files recursively.");
        System.out.println("  touch <filename> - Create a new file.");
        System.out.println("  mv <source> <destination> - Move or rename a file.");
        System.out.println("  echo <text> > <filename> - Write text to a file.");
        System.out.println("  echo <text> >> <filename> - Append text to a file.");
        System.out.println("  exit         - Exit the CLI.");
        System.out.println("  help         - Display this help message.");
    }
}
