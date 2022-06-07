package org.zdulski.finalproject.view_controllers;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class MealController implements Initializable {
    //TODO differ between random meal and searched meal
    //TODO add go back button for searched meal

    protected Meal meal;

    @FXML
    private Text area;

    @FXML
    private Text category;

    @FXML
    private Text name;

    @FXML
    private Text tags;

    @FXML
    private ImageView thumbnail;

    @FXML
    private Hyperlink ytLink;

    @FXML
    private TableColumn ingredientCol;

    @FXML
    private TableColumn ingredientMeasureCol;

    @FXML
    private TableView ingredientsTable;

    @FXML
    private TextArea executionText;


    @FXML
    private ImageView favouriteIcon;

    @FXML
    private ImageView nextArrowIcon;

    @FXML
    private ImageView returnIcon;

    @FXML
    public void addToFavourite(){
        //TODO implement adding to favourites
        System.out.println("add to favourite clicked");
    }

    public MealController(){
        EventBusFactory.getEventBus().register(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            favouriteIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/icons/heart-empty.png")));
            nextArrowIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/icons/arrow-right.png")));
            returnIcon.setImage(new Image(new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/org/zdulski/finalproject/icons/arrow-return.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void getRandomMeal() {
        CompletableFuture<Meal> future = CompletableFuture.supplyAsync(() -> {
            //TODO add loading animation - custom component?
            return new MealGetterImpl().getRandomMeal();
        }).thenApply(result -> {
            meal = result;
            showMeal();
            return result;
        });
    }

    @FXML
    public void onYtLinkClick(){
        //TODO implement onYyLinkClick()
        System.out.println("link clicked");
    }

    @FXML
    public void returnClicked(){
        System.out.println("return clicked");
    }

    private void showMeal() {
        //TODO set condition to not display null or empty values
        name.setText(meal.getName());
        category.setText(meal.getCategory());
        area.setText(meal.getArea());
        tags.setText(meal.getTags());
        thumbnail.setImage(new Image(meal.getThumbnail()));
        setIngredientsTableView(meal.getIngredients());
        executionText.setText(meal.getInstructions());
    }

    private void setIngredientsTableView(Map<String, String> ingredients){
        List<Map<String, String>> list = new ArrayList<>();
        for (var ingredient : ingredients.keySet()){
            Map<String,String> temp = new HashMap<>();
            temp.put("Ingredient", ingredient);
            temp.put("Measure", ingredients.get(ingredient));
            list.add(temp);
        }
        //TODO resize font - should be bigger
        //TODO wrapping when ingredient or its measure value is to long to fit (now it get cut with '...')
        ingredientCol.setCellValueFactory(new MapValueFactory<>("Ingredient"));
        ingredientMeasureCol.setCellValueFactory(new MapValueFactory<>("Measure"));
        ingredientsTable.getItems().addAll(list);
    }

    @Subscribe
    public void setMeal(Meal meal) {
        this.meal = meal;
        System.out.println(meal);
        showMeal();
    }
}
