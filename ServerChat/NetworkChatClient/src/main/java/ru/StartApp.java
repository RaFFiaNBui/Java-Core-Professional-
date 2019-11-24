package ru;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.controller.Controller;

import java.io.IOException;

public class StartApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("myJavaFXChat");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Controller controller = fxmlLoader.getController();
        stage.setOnHidden(e -> controller.shutdown());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
