package com.example.consumptionmanager.util;

import com.example.consumptionmanager.model.Consumption;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {
    private static final String CSV_FILE = "consumptions.csv";
    private static final String CSV_SEPARATOR = ",";

    public static void saveConsumption(Consumption consumption) {
        try (FileWriter fw = new FileWriter(CSV_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            out.println(consumption.getId() + CSV_SEPARATOR +
                    consumption.getName() + CSV_SEPARATOR +
                    consumption.getAmount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Consumption> loadConsumptions() {
        List<Consumption> consumptions = new ArrayList<>();
        File file = new File(CSV_FILE);

        if (!file.exists()) {
            return consumptions;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(CSV_SEPARATOR);
                if (values.length == 3) {
                    int id = Integer.parseInt(values[0]);
                    String name = values[1];
                    double amount = Double.parseDouble(values[2]);
                    consumptions.add(new Consumption(id, name, amount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return consumptions;
    }

    public static void updateConsumption(Consumption updatedConsumption) {
        List<Consumption> consumptions = loadConsumptions();
        for (int i = 0; i < consumptions.size(); i++) {
            if (consumptions.get(i).getId() == updatedConsumption.getId()) {
                consumptions.set(i, updatedConsumption);
                break;
            }
        }
        saveAllConsumptions(consumptions);
    }

    private static void saveAllConsumptions(List<Consumption> consumptions) {
        try (FileWriter fw = new FileWriter(CSV_FILE);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            for (Consumption c : consumptions) {
                out.println(c.getId() + CSV_SEPARATOR +
                        c.getName() + CSV_SEPARATOR +
                        c.getAmount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNextId() {
        List<Consumption> consumptions = loadConsumptions();
        if (consumptions.isEmpty()) {
            return 1;
        }
        return consumptions.get(consumptions.size() - 1).getId() + 1;
    }
}