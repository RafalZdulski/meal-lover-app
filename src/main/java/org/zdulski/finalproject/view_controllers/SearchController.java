package org.zdulski.finalproject.view_controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdulski.finalproject.MainApplication;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ReturnEvent;
import org.zdulski.finalproject.eventbus.ShowMealsByIdsEvent;
import org.zdulski.finalproject.eventbus.ShowMealsEvent;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;
import org.zdulski.finalproject.mealdbAPI.SearchEngineImpl;
import org.zdulski.finalproject.view_auxs.loading.LoadingCircles;
import org.zdulski.finalproject.view_auxs.search.filters.FilterPillsDisplay;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWindow;
import org.zdulski.finalproject.view_auxs.search.filters.FilterWrap;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SearchController implements Initializable {

    private List<FilterWrap> areas;

    private List<FilterWrap> categories;

    private LoadingCircles loadingCircles;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private Button addAreaBtn;

    @FXML
    private Button addCategoryBtn;

    @FXML
    private Button clearAllBtn;

    @FXML
    private Button clearAreaBtn;

    @FXML
    private Button clearCategoryBtn;

    @FXML
    private Button clearNameBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField nameField;

    @FXML
    private VBox filterPillsVBox;

    @FXML
    private Button returnBtn;

    private final static Logger LOG = LogManager.getLogger(MainApplication.class);

    @FXML
    void addArea() {
        new FilterWindow(areas);
    }

    @FXML
    void addCategory() {
        new FilterWindow(categories);
    }

    @FXML
    void clearAll() {
        clearName();
        clearAreas();
        clearCategories();
    }

    @FXML
    void clearAreas() {
        areas.forEach(f -> f.setCheck(false));
    }

    @FXML
    void clearCategories() {
        categories.forEach(f -> f.setCheck(false));
    }

    @FXML
    void clearName() {
        nameField.clear();
    }

    @FXML
    void searchBtnClicked() {
        loadingCircles.setVisible(true);
        final String[] wordsFilters = nameField.getText().length()==0? new String[0] : nameField.getText().split("\\s+");
        final String[] areasFilter = areas.stream().filter(FilterWrap::getCheckValue).map(FilterWrap::toString).toArray(String[]::new);
        final String[] categoryFilters = categories.stream().filter(FilterWrap::getCheckValue).map(FilterWrap::toString).toArray(String[]::new);
        LOG.info("search vector:\n\twords:\t" + Arrays.toString(wordsFilters) + "\n\tareas:\t" + Arrays.toString(areasFilter) + "\n\tcategories:\t" + Arrays.toString(categoryFilters));

        //get all meals when filtered by empty filters
        if (wordsFilters.length + areasFilter.length + categoryFilters.length < 1) {
            CompletableFuture.runAsync(()-> {
                List<Meal> meals = new MealGetterImpl().getAllMeals();
                EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.BROWSE));
            });
            return;
        }

        CompletableFuture.supplyAsync(() ->
                new SearchEngineImpl().getIDs(wordsFilters, areasFilter, categoryFilters)
        ).thenApply(ids -> {
            loadingCircles.setVisible(false);
            if (!ids.isEmpty()) {
//                List<Meal> meals = new MealGetterImpl().getMealsByIds(ids);
                LOG.info("search engine found " + ids.size() + " recipes");
//                EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.BROWSE));
                EventBusFactory.getEventBus().post(new ShowMealsByIdsEvent(ids, View.BROWSE));
            } else {
                LOG.info("search engine found nothing");
                showMessage("nothing found!");
            }
            return ids;
        });
    }

    private void showMessage(String message) {
        double WIDTH = mainPane.getWidth();
        int messageHeight = 300;
        Text text = new Text(message);
        text.setFont(Font.font("system", FontWeight.BOLD, FontPosture.ITALIC,18));
        text.setFill(Color.DARKRED);
        HBox popup = new HBox(text);
        popup.setLayoutX(WIDTH);
        popup.setLayoutY(messageHeight);
        popup.setPrefWidth(180);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(7));
        popup.setStyle("-fx-background-color: pink; -fx-background-radius: 15 0 0 15");
        KeyValue heightVal = new KeyValue(popup.layoutXProperty(), WIDTH-popup.getPrefWidth());
        KeyFrame heightFrame = new KeyFrame(Duration.millis(750), heightVal);
        Timeline show = new Timeline(heightFrame);
        KeyValue heightVal2 = new KeyValue(popup.layoutXProperty(), WIDTH);
        KeyFrame heightFrame2 = new KeyFrame(Duration.millis(750), heightVal2);
        Timeline hide = new Timeline(heightFrame2);

        Platform.runLater(() -> mainPane.getChildren().add(popup));

        new Thread(() -> {
            show.play();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            hide.play();
        }).start();

    }

    @FXML
    public void returnBtnClicked(){
        EventBusFactory.getEventBus().post(new ReturnEvent());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MealGetterImpl dbAccess = new MealGetterImpl();
        areas = dbAccess.getAreas().stream().map(FilterWrap::new).collect(Collectors.toList());
        categories = dbAccess.getCategories().stream().map(FilterWrap::new).collect(Collectors.toList());

        filterPillsVBox.getChildren().add(new FilterPillsDisplay(areas));
        filterPillsVBox.getChildren().add(new FilterPillsDisplay(categories));

        returnBtn.setGraphic(getButtonIcon("arrow-return-right.png"));
        addAreaBtn.setGraphic(getButtonIcon("add-icon.png"));
        addCategoryBtn.setGraphic(getButtonIcon("add-icon.png"));
        clearAllBtn.setGraphic(getButtonIcon("backspace.png"));
        clearAreaBtn.setGraphic(getButtonIcon("backspace.png"));
        clearCategoryBtn.setGraphic(getButtonIcon("backspace.png"));
        clearNameBtn.setGraphic(getButtonIcon("backspace.png"));
        searchBtn.setGraphic(getButtonIcon("magnifying-glass-icon.png"));

        loadingCircles = initLoadingCircles();
        loadingCircles.setVisible(false);
    }

    public SearchController(){

    }

    private ImageView getButtonIcon(String name){
        Image image = new Image(System.getProperty("user.dir") + "/"
                + PropertyManager.getInstance().getProperty("iconsFolder") + "/" + name);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
        imageView.setSmooth(true);
        imageView.setOpacity(0.80);
        return imageView;
    }

    private LoadingCircles initLoadingCircles(){
        double radius = 70;
        int amountOfCircles = 8;
        double circlesRadius = Math.PI*radius/amountOfCircles-6;
        LoadingCircles circles = new LoadingCircles(radius, amountOfCircles, circlesRadius,
                2000, 4000);
        circles.setLayoutX(60);
        circles.setLayoutY(200);
        mainPane.getChildren().add(circles);
        circles.play();
        return circles;
    }
}
