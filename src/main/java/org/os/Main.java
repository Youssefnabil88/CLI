package org.os;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        //Do not forget to change the init PATH
        String initialPath = "E:/os cli new-edition/CLI";
        File initialDir = new File(initialPath);

        if (!initialDir.exists() || !initialDir.isDirectory()) {
            System.out.println("Error: The specified directory does not exist.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.setProperty("user.dir", initialPath);

        while (true) {
            // System.out.print(initialDir.getAbsolutePath() + " >> ");
            System.out.print(System.getProperty("user.dir") + " >> ");
            String input = scanner.nextLine();
            String[] commandArgs = input.split("\\s+");

            if (commandArgs.length > 0) {
                if ("exit".equalsIgnoreCase(commandArgs[0])) {
                    System.out.println("Exiting CLI...");
                    break;
                }
                else if (input.contains("|")) {
                    CommandExecution.PipeCommand(commandArgs);}
                    else
                handleCommand(commandArgs);
            }
        }
        scanner.close();
    }

    private static void handleCommand(String[] commandArgs) {
        try {
            // Check for output redirection with >
            if (commandArgs.length > 2 && ">".equals(commandArgs[commandArgs.length - 2])) {
                String[] outputCommand = new String[commandArgs.length - 2];
                System.arraycopy(commandArgs, 0, outputCommand, 0, commandArgs.length - 2);
                String fileName = commandArgs[commandArgs.length - 1];
                writeToFile(outputCommand, fileName);
                return;
            }

// if{}
            if ("ls".equals(commandArgs[0])) {
                if (commandArgs.length > 1 && "-a".equals(commandArgs[1])) {
                    CommandExecution.listDirectoryWithHidden (commandArgs);
                }

            }   if (Arrays.asList(commandArgs).contains("|")) {
                CommandExecution.PipeCommand(commandArgs);
               return;
            }
            
            else {
                //pwd cd ls
                // Handle other commands
                CommandExecution.execute(commandArgs);
            }
        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }


    // Method to write output to a file, overwriting existing content
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
}
