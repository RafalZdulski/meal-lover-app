package org.zdulski.finalproject.viewcontrollers;

import javafx.fxml.FXML;
import org.zdulski.finalproject.mealdb.ApiController;
import org.zdulski.finalproject.mealdb.dto.Meal;

public class DrawerController {
    @FXML
    public void onRandomClick(){
        System.out.println("random clicked");
        ApiController apiController = new ApiController();
        Meal meal = apiController.getRandomMeal();
        System.out.println("meal:\n"+meal);
    }

    @FXML
    public void onSearchClick(){
        System.out.println("search clicked");
    }

    @FXML
    public void onExitClick(){
       System.exit(0);
    }

}
