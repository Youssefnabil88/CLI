package org.os;

import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
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
                    CommandExecution.execute(commandArgs);
                }
            }
        }
    }
}
