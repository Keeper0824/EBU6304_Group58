package com.example.consumptionmanager.controllers;

import com.example.consumptionmanager.model.Consumption;
import com.example.consumptionmanager.util.CSVUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private TableView<Consumption> consumptionTable;
    @FXML
    private TableColumn<Consumption, Integer> idColumn;
    @FXML
    private TableColumn<Consumption, String> nameColumn;
    @FXML
    private TableColumn<Consumption, Double> amountColumn;

    private ObservableList<Consumption> consumptionData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // 初始化表格列
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        // 加载数据
        loadConsumptionData();
    }

    private void loadConsumptionData() {
        consumptionData.clear();
        consumptionData.addAll(CSVUtil.loadConsumptions());
        consumptionTable.setItems(consumptionData);
    }

    @FXML
    private void handleAddConsumption() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consumptionmanager/views/AddConsumption.fxml"));
            Parent root = loader.load();

            AddConsumptionController controller = loader.getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setTitle("添加消费记录");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(consumptionTable.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditConsumption() {
        Consumption selectedConsumption = consumptionTable.getSelectionModel().getSelectedItem();
        if (selectedConsumption != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/consumptionmanager/views/EditConsumption.fxml"));
                Parent root = loader.load();

                EditConsumptionController controller = loader.getController();
                controller.setMainController(this);
                controller.setConsumption(selectedConsumption);

                Stage stage = new Stage();
                stage.setTitle("编辑消费记录");
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(consumptionTable.getScene().getWindow());
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshTable() {
        loadConsumptionData();
    }
}