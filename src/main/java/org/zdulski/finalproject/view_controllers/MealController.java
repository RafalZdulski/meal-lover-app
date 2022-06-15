package org.zdulski.finalproject.view_controllers;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.data.dto.UserProxy;
import org.zdulski.finalproject.eventbus.*;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;

import java.net.URL;
import java.util.*;

public class MealController implements ViewController {

    protected Meal meal;

    private View sourceView;

    @FXML
    private Text area;

    @FXML
    private Text category;

    @FXML
    private Text name;

    @FXML
    private Text tags;

    @FXML
    private Button rightBtn;

    @FXML
    private Rectangle thumbnailRect;

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
    private ImageView rightBtnIcon;

    @FXML
    private ImageView returnIcon;

    private String iconsFolder = System.getProperty("user.dir") + "/" +
            PropertyManager.getInstance().getProperty("iconsFolder") + "/" ;

    public MealController(){
        EventBusFactory.getEventBus().register(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        favouriteIcon.setImage(new Image( iconsFolder + "favorite-outline.png"));
        returnIcon.setImage(new Image(iconsFolder + "arrow-return.png"));
        //setRightActionBtn(Action.DISABLE);
    }

    @FXML
    public void addToFavourite(){
        EventBusFactory.getEventBus().post(new AddToFavouriteEvent(meal));
        if (UserProxy.getInstance().isFavourite(meal.getId()))
            favouriteIcon.setImage(new Image( iconsFolder + "favorite.png"));
        else
            favouriteIcon.setImage(new Image( iconsFolder + "favorite-outline.png"));
    }

    @FXML
    public void onYtLinkClick(){
        //TODO ADD implement onYtLinkClick()
        System.out.println("link clicked");
    }

    @FXML
    public void returnClicked(){
        EventBusFactory.getEventBus().post(new ReturnEvent());
    }

    private void showMeal() {
        name.setText(meal.getName());
        category.setText(meal.getCategory());
        area.setText(meal.getArea());
        tags.setText(meal.getTags());//TODO FIX set condition to not display null or empty values
        thumbnailRect.setFill(new ImagePattern(new Image(meal.getThumbnail())));
        setIngredientsTableView(meal.getIngredients());
        executionText.setText(meal.getInstructions());

        if (UserProxy.getInstance().isFavourite(meal.getId())){
            favouriteIcon.setImage(new Image( iconsFolder + "favorite.png"));
        }
    }

    private void setIngredientsTableView(Map<String, String> ingredients){
        List<Map<String, String>> list = new ArrayList<>();
        for (String ingredient : ingredients.keySet()){
            Map<String,String> temp = new HashMap<>();
            temp.put("Ingredient", ingredient);
            temp.put("Measure", ingredients.get(ingredient));
            list.add(temp);
        }
        //TODO BEAUTIFY resize font - should be bigger
        //TODO FIX wrapping when ingredient or its measure value is to long to fit (now it get cut with '...')
        ingredientCol.setCellValueFactory(new MapValueFactory<>("Ingredient"));
        ingredientMeasureCol.setCellValueFactory(new MapValueFactory<>("Measure"));
        ingredientsTable.getItems().addAll(list);
    }

    @Subscribe
    public void setMeal(Meal meal) {
        this.meal = meal;
        showMeal();
    }

    @Override
    public void onEntering() {

    }

    @Override
    public void update() {

    }

    @Override
    public void onLeaving() {

    }

    public void setRightActionBtn(Action action){
        switch (action){
            case NEXT_MEAL -> {
                rightBtnIcon.setImage(new Image(iconsFolder + "arrow-right.png"));
                rightBtn.setOnAction(e -> {
                    EventBusFactory.getEventBus().post(new NextMealEvent(meal, sourceView));
                });
            }
            case RANDOM_MEAL -> {
                rightBtnIcon.setImage(new Image(iconsFolder + "dice.png"));
                rightBtn.setOnAction(e -> {
                    Meal meal = new MealGetterImpl().getRandomMeal();
                    EventBusFactory.getEventBus().post(new ShowMealEvent(meal, Action.RANDOM_MEAL, View.MEAL));
                });
            }

            case YOUTUBE -> {
                rightBtnIcon.setImage(new Image(iconsFolder + "youtube.png"));
                rightBtn.setOnAction(e -> {
                    onYtLinkClick();
                });
            }
            case DISABLE -> {
                rightBtnIcon.setImage(new Image(iconsFolder + "arrow-right.png"));
                rightBtn.setDisable(true);
            }
        }
    }

    public void setSourceView(View sourceView) {
        this.sourceView = sourceView;
    }

    public enum Action{
        NEXT_MEAL,
        RANDOM_MEAL,
        DISABLE,
        YOUTUBE
    }
}
