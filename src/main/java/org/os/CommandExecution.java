package org.os;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
            default:
                output.append("Unknown command: ").append(command[0]).append("\n");
        }
        return output.toString();
    }
}
