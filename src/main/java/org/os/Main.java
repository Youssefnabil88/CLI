package org.os;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        // Change the initial path to your preferred starting directory
        String initialPath = "/Users/dodoa/OneDrive/Desktop/OS"; 
        File initialDir = new File(initialPath);

        if (!initialDir.exists() || !initialDir.isDirectory()) {
            System.out.println("Error: The specified directory does not exist.");
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            System.setProperty("user.dir", initialPath);
            
            while (true) {
                System.out.print(initialDir.getAbsolutePath() + " >> ");
                String input = scanner.nextLine();
                String[] commandArgs = input.split("\\s+");
                
                if (commandArgs.length > 0) {
                    if ("exit".equalsIgnoreCase(commandArgs[0])) {
                        System.out.println("Exiting CLI...");
                        break;
                    }
                    handleCommand(commandArgs);
                }
            }
        }
    }

    private static void handleCommand(String[] commandArgs) {
        try {
            if (commandArgs.length > 2) {
                if (">".equals(commandArgs[commandArgs.length - 2])) {
                    String[] outputCommand = new String[commandArgs.length - 2];
                    System.arraycopy(commandArgs, 0, outputCommand, 0, commandArgs.length - 2);
                    String fileName = commandArgs[commandArgs.length - 1];
                    writeToFile(outputCommand, fileName, false); // overwrite mode
                    return;
                } else if (">>".equals(commandArgs[commandArgs.length - 2])) {
                    String[] outputCommand = new String[commandArgs.length - 2];
                    System.arraycopy(commandArgs, 0, outputCommand, 0, commandArgs.length - 2);
                    String fileName = commandArgs[commandArgs.length - 1];
                    writeToFile(outputCommand, fileName, true); // append mode
                    return;
                }
            }

            if ("ls".equals(commandArgs[0])) {
                if (commandArgs.length > 1 && "-r".equals(commandArgs[1])) {
                    CommandExecution.listDirectoryRecursive();
                } else if (commandArgs.length > 1 && "-a".equals(commandArgs[1])) {
                    CommandExecution.listDirectoryWithHidden(commandArgs);
                } else {
                    CommandExecution.listDirectory();
                }
            } else {
                // Handle other commands
                CommandExecution.execute(commandArgs);
            }
        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }

    public static void writeToFile(String[] command, String fileName, boolean append) {
        try {
            File file = new File(System.getProperty("user.dir"), fileName);
            try (FileWriter writer = new FileWriter(file, append)) {
                String output = CommandExecution.executeCommandAndGetOutput(command);
                writer.write(output);
                writer.write(System.lineSeparator()); // Add a newline
                System.out.println("Output " + (append ? "appended to" : "written to") + " " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    
}
