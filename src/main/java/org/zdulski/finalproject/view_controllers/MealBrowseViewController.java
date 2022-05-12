package org.zdulski.finalproject.view_controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.zdulski.finalproject.mealdb.dto.Meal;

public class MealBrowseViewController {
    private Meal meal;

    @FXML
    private Text area;

    @FXML
    private Text category;

    @FXML
    private ImageView thumbnail;

    @FXML
    private Text name;

    @FXML
    private Text tags;

    public void setMeal(Meal meal){
        this.meal = meal;
        showMeal();
    }

    private void showMeal() {
        //TODO set condition to not display null or empty values
        name.setText(meal.getName());
        category.setText(meal.getCategory());
        area.setText(meal.getArea());
        tags.setText(meal.getTags());
        thumbnail.setImage(new Image(meal.getThumbnail()));
    }
}
