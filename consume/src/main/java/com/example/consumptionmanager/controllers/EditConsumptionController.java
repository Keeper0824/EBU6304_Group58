package com.example.consumptionmanager.controllers;

import com.example.consumptionmanager.model.Consumption;
import com.example.consumptionmanager.util.CSVUtil;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EditConsumptionController {
    @FXML
    private TextField nameField;
    @FXML
    private TextField amountField;

    private Consumption consumption;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
        nameField.setText(consumption.getName());
        amountField.setText(String.valueOf(consumption.getAmount()));
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            consumption.setName(nameField.getText());
            consumption.setAmount(Double.parseDouble(amountField.getText()));

            CSVUtil.updateConsumption(consumption);
            mainController.refreshTable();

            // 关闭窗口
            nameField.getScene().getWindow().hide();
        }
    }

    @FXML
    private void handleCancel() {
        nameField.getScene().getWindow().hide();
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