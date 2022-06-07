package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealEvent;
import org.zdulski.finalproject.eventbus.ShowMealsEvent;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class DrawerController implements Initializable {
    @FXML
    ImageView drawerLogo;

    @FXML
    ImageView drawerFiller;

    @FXML
    public void onRandomClick(){
        EventBusFactory.getEventBus().post(
                new ShowMealEvent(new MealGetterImpl().getRandomMeal())
        );
    }


    @FXML
    public void onBrowseClick(){
        //EventBusFactory.getEventBus().post(ChangeViewEvent.BROWSE);
        EventBusFactory.getEventBus().post(new ShowMealsEvent(new MealGetterImpl().getAllMeals()));
        System.out.println("browse clicked");
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
