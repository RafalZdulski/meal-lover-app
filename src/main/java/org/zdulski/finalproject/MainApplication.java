package org.zdulski.finalproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.zdulski.finalproject.dto.User;
import org.zdulski.finalproject.dto.UserProxy;

import java.io.IOException;

public class MainApplication extends Application {
    public final int HEIGHT = 640;
    public final int WIDTH = 360;

    @Override
    public void start(Stage stage) throws IOException {
        UserProxy.getInstance().setUser(new User("default user"));

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle("MEALover!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}