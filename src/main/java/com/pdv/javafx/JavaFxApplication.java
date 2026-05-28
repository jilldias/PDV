package com.pdv.javafx;

import com.pdv.PdvApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        SpringApplication application = new SpringApplication(PdvApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        springContext = application.run();
    }

    @Override
    public void start(Stage stage) {
        StageManager stageManager = springContext.getBean(StageManager.class);
        stageManager.setPrimaryStage(stage);
        stageManager.showScene("/fxml/login.fxml", "PDV Desktop - Login", false);
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
