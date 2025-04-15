package main.controller;

import model.User;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserController {
    private static final String CSV_FILE = "user_data.csv";

    public UserController() {
        initializeCSVFile();
    }

    private void initializeCSVFile() {
        try {
            Path path = Paths.get(CSV_FILE);
            if (!Files.exists(path)) {
                Files.createFile(path);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
                    writer.write("\"Username\",\"Password\",\"Registration Time\"");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing CSV file: " + e.getMessage());
        }
    }

    public boolean saveUser(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            writer.write(user.toCSV());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error saving user data: " + e.getMessage());
            return false;
        }
    }
}