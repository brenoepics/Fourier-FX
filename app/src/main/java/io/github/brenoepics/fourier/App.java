package io.github.brenoepics.fourier;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 830, 400);
        stage.setMinWidth(535);
        stage.setMaxWidth(2040);
        stage.setMinHeight(400);
        stage.setMaxHeight(400);
        stage.setTitle("Fourier Series");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

