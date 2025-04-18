package com.example.consumptionmanager.controllers;

import com.example.consumptionmanager.model.Consumption;
import com.example.consumptionmanager.util.CSVUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddConsumptionController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;

    private MainController mainController;
    private Stage dialogStage;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void handleAdd() {
        if (isInputValid()) {
            String name = nameField.getText();
            double amount = Double.parseDouble(amountField.getText());
            int id = CSVUtil.getNextId();

            Consumption consumption = new Consumption(id, name, amount);
            CSVUtil.saveConsumption(consumption);

            mainController.refreshTable();
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "消费名称不能为空！\n";
        }

        if (amountField.getText() == null || amountField.getText().isEmpty()) {
            errorMessage += "消费金额不能为空！\n";
        } else {
            try {
                Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "消费金额必须是数字！\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            // 显示错误消息
            return false;
        }
    }
}