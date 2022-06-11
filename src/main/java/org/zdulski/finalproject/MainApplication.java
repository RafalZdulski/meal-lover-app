package org.zdulski.finalproject;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.User;
import org.zdulski.finalproject.data.dto.UserProxy;
import org.zdulski.finalproject.data.repository.NoSuchUserException;
import org.zdulski.finalproject.data.repository.UserAlreadyExistsException;
import org.zdulski.finalproject.data.repository.UserRepository;
import org.zdulski.finalproject.data.repository.UserRepositoryImpl;

import java.io.IOException;

public class MainApplication extends Application {
    public final int HEIGHT = 640;
    public final int WIDTH = 360;

    @Getter
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void start(Stage stage) throws IOException {

        UserProxy.getInstance().setUser(getDefaultUser());

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle("MEALover!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private User getDefaultUser(){
        //TODO ADD ability to switch users and login view
        User defaultUser = null;
        String defaultUsername = PropertyManager.getInstance().getProperty("defaultUsername");
        UserRepository userRepo = new UserRepositoryImpl();
        try {
            defaultUser = userRepo.newUser(defaultUsername);
        } catch (UserAlreadyExistsException e) {
            System.out.println(defaultUsername + " already exists - loading profile");
            try {
                defaultUser = userRepo.getUser(defaultUsername);
            } catch (NoSuchUserException ex) {
                //should never occur!
                ex.printStackTrace();
            }
        }

        try {
            System.out.println("\tHELLO\n" + userRepo.getUser(defaultUsername));
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }

        return defaultUser;
    }
}