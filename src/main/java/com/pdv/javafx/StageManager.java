package com.pdv.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class StageManager {

    private final ApplicationContext applicationContext;
    private Stage primaryStage;

    public StageManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void showScene(String fxmlPath, String title, boolean resizable) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(applicationContext::getBean);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.setResizable(resizable);
            primaryStage.show();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao abrir cena: " + fxmlPath, e);
        }
    }
}
