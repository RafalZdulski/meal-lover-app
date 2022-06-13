package org.zdulski.finalproject;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.User;
import org.zdulski.finalproject.data.dto.UserProxy;
import org.zdulski.finalproject.data.repository.NoSuchUserException;
import org.zdulski.finalproject.data.repository.UserAlreadyExistsException;
import org.zdulski.finalproject.data.repository.UserRepository;
import org.zdulski.finalproject.data.repository.UserRepositoryImpl;

import java.io.File;
import java.io.IOException;

public class MainApplication extends Application {
    public final int HEIGHT = 640;
    public final int WIDTH = 360;

    private final static Logger LOG = LogManager.getLogger(MainApplication.class);

    @Getter
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    @Override
    public void start(Stage stage) throws IOException {
        UserProxy.getInstance().setUser(this.getDefaultUser());

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("views/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), WIDTH, HEIGHT);
        stage.setTitle("MEALover!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        //configuring logger
        LoggerContext context = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
        File file = new File("./src/main/resources/log4j2.xml");
        context.setConfigLocation(file.toURI());

        launch();
    }

    private User getDefaultUser(){
        //TODO ADD ability to switch users

        User defaultUser = null;
        String defaultUsername = PropertyManager.getInstance().getProperty("defaultUsername");
        UserRepository userRepo = new UserRepositoryImpl();
        try {
            LOG.info("loading default user: '" + defaultUsername + "'");
            defaultUser = userRepo.newUser(defaultUsername);
        } catch (UserAlreadyExistsException e) {
            LOG.info("default user: '" + defaultUsername + "' already exists - loading profile");
            try {
                LOG.info("default user: '" + defaultUsername + "' not found - creating new profile");
                defaultUser = userRepo.getUser(defaultUsername);
            } catch (NoSuchUserException ex) {
                LOG.fatal("fatal error logging in as default user: '" + defaultUsername + "'");
                ex.printStackTrace();
            }
        }

        try {
//            System.out.println("\tHELLO\n" + userRepo.getUser(defaultUsername));
            LOG.info("logged in as: '" + userRepo.getUser(defaultUsername) + "'");
        } catch (NoSuchUserException e) {
            e.printStackTrace();
        }

        return defaultUser;
    }
}