package src.main.java;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateUserFile {
    public static void main(String[] args) {
        String csvFile = "users.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            bw.write("Nickname,Password,Email,Gender,DateOfBirth");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}