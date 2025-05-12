package src.main.java.Category_story6_7;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AnalysisReportController {
    @FXML
    private VBox reportContent;

    public void setReportText(String reportText) {
        Label reportLabel = new Label(reportText);
        reportLabel.setWrapText(true);
        reportLabel.setFont(new Font(14));
        reportContent.getChildren().add(reportLabel);
    }

    @FXML
    private void handleClose() {
        // 关闭窗口
        Stage stage = (Stage) reportContent.getScene().getWindow();
        stage.close();
    }
}