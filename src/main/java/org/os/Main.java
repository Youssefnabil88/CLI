package org.os;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Do not forget to change the init PATH
        String initialPath = "/home/youssef";
        File initialDir = new File(initialPath);

        if (!initialDir.exists() || !initialDir.isDirectory()) {
            System.out.println("Error: The specified directory does not exist.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.setProperty("user.dir", initialPath);

        while (true) {
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
                CommandExecution.writeToFile(outputCommand, fileName);
                return;
            }

            if ("ls".equals(commandArgs[0])) {
                if (commandArgs.length > 1 && "-a".equals(commandArgs[1])) {
                    CommandExecution.listDirectoryWithHidden (commandArgs);
                }
                else if (commandArgs.length > 1 && "-r".equals(commandArgs[1])) {
                    CommandExecution.ls(commandArgs);
                }
                else{
                    CommandExecution.execute(commandArgs);
                }
            }
            else if (Arrays.asList(commandArgs).contains("|")) {
                CommandExecution.PipeCommand(commandArgs);
            }
            else {
                // Handle other commands
                CommandExecution.execute(commandArgs);
            }
        } catch (Exception e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }


}
