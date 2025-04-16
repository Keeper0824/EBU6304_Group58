module BankCardFX {
    // 声明依赖的JavaFX模块
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.fxml;

    // 开放你的包给JavaFX框架
    opens card_management to javafx.graphics, javafx.fxml;

    // 允许反射访问（用于TableView等组件）
    exports card_management to javafx.graphics, javafx.base;
}