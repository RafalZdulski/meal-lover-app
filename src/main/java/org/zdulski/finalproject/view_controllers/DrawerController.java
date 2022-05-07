package org.zdulski.finalproject.view_controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.zdulski.finalproject.mediator_controllers.ViewMediator;

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
        System.out.println("random clicked");
        CompletableFuture<FXMLLoader> futurePane = CompletableFuture.supplyAsync(new Supplier<FXMLLoader>() {
            @Override
            public FXMLLoader get() {
//                try {
//                    TimeUnit.SECONDS.sleep(5);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                return new FXMLLoader(getClass().getResource("/org/zdulski/finalproject/views/meal-view.fxml"));
            }
        }).thenApply(loader -> {
            Platform.runLater( () -> {
                try {
                    Pane mealPane = loader.load();
                    ViewMediator.getInstance().setCenter(mealPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return loader;
        }).thenApply(loader -> {
            Platform.runLater(() ->{
                MealController controller = loader.getController();
                System.out.println(controller);
                controller.getRandomMeal();
            });
            return loader;
        });

//        try {
//            //TODO RETHINK if Im handling communication by mediator then shouldn't this be also moved to mediator
//            //or maybe it should be realized by listener/observer
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/zdulski/finalproject/views/meal-view.fxml"));
//            Pane mealViewPane = loader.load();
//            ViewMediator.getInstance().setCenter(mealViewPane);
//            MealController mealController = loader.getController();
//            mealController.getRandomMeal();
//            mealController = loader.getController();
//            System.out.println("3");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }


    @FXML
    public void onBrowseClick(){
        System.out.println("browse clicked");
    }

    @FXML
    public void onFavouriteClick(){
        System.out.println("favourites clicked");
    }

    @FXML
    public void onLastViewedClick(){
        System.out.println("favourites clicked");
    }

    @FXML
    public void onInspirationClick(){
        System.out.println("favourites clicked");
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
