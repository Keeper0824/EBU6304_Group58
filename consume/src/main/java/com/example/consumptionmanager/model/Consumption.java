package com.example.consumptionmanager.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Consumption {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty amount;

    public Consumption(int id, String name, double amount) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.amount = new SimpleDoubleProperty(amount);
    }

    // Getters and setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getAmount() {
        return amount.get();
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    // Property getters
    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }
}