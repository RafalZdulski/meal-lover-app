package org.zdulski.finalproject.view_controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.zdulski.finalproject.mediators.MainMediator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DrawerController implements Initializable {
    @FXML
    ImageView drawerLogo;

    @FXML
    ImageView drawerFiller;

    @FXML
    public void onRandomClick(){
        CompletableFuture<FXMLLoader> futurePane = CompletableFuture.supplyAsync(new Supplier<FXMLLoader>() {
            @Override
            public FXMLLoader get() {
                return new FXMLLoader(getClass().getResource("/org/zdulski/finalproject/views/meal-view.fxml"));
            }
        }).thenApply(loader -> {
            Platform.runLater( () -> {
                try {
                    Pane mealPane = loader.load();
                    MainMediator.getInstance().setCenter(mealPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return loader;
        }).thenApply(loader -> {
            Platform.runLater(() ->{
                MealController controller = loader.getController();
                controller.getRandomMeal();
            });
            return loader;
        });
    }


    @FXML
    public void onBrowseClick(){
        CompletableFuture<FXMLLoader> futurePane = CompletableFuture.supplyAsync(new Supplier<FXMLLoader>() {
            @Override
            public FXMLLoader get() {
                return new FXMLLoader(getClass().getResource("/org/zdulski/finalproject/views/browse-view.fxml"));
            }
        }).thenApply(loader -> {
            Platform.runLater( () -> {
                try {
                    Pane mealPane = loader.load();
                    MainMediator.getInstance().setCenter(mealPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return loader;
        }).thenApply(loader -> {
            Platform.runLater(() ->{
                BrowseController controller = loader.getController();
                controller.getAllMeals();
            });
            return loader;
        });
    }

    @FXML
    public void onFavouriteClick(){
        System.out.println("favourites clicked");
    }

    @FXML
    public void onLastViewedClick(){
        System.out.println("last viewed clicked");
    }

    @FXML
    public void onInspirationClick(){
        System.out.println("inspiration clicked");
    }

    @FXML
    public void onExitClick(){
       System.exit(0);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //relative path does not work - I do not know exactly why
            //TODO drawerFiller should be filling empty space between drawer options (now its dimensions are calculated by hand)
            drawerFiller.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/images/drawer-food-filler.jpg")));
            drawerLogo.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/images/food-lover-red-banner.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
