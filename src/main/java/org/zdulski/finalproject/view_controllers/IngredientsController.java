package org.zdulski.finalproject.view_controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealsEvent;
import org.zdulski.finalproject.mealdbAPI.MealGetterImpl;
import org.zdulski.finalproject.mealdbAPI.SearchEngineImpl;
import org.zdulski.finalproject.view_auxs.loading.LoadingCircles;
import org.zdulski.finalproject.view_auxs.search.filters.FilterPillsDisplay;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class IngredientsController implements ViewController{

    private final static Logger LOG = LogManager.getLogger(IngredientsController.class);

    @FXML
    private AnchorPane mainPane;

    private List<String> allIngredients;

    private ObservableList<String> filteredIngredients;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<String> ingredientsTable;

    @FXML
    private TableColumn<String, String> nameCol;

    @FXML
    private TableColumn addCol;

    @FXML
    private ScrollPane fridgePane;

    private FilterPillsDisplay fridge;

    private LoadingCircles loadingCircles;

    public IngredientsController(){
    }

    @Override
    public void onEntering() {

    }

    @Override
    public void update() {}

    @Override
    public void onLeaving() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CompletableFuture.supplyAsync(() -> {
            allIngredients = new MealGetterImpl().getIngredients();
            return allIngredients;
        }).thenApply(allIngredients -> {
            filteredIngredients = FXCollections.observableList(allIngredients);
            ingredientsTable.setItems(filteredIngredients);
            return allIngredients;
        }).thenRunAsync(() -> {
            nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
            addCol.setCellFactory(this.getBtnFactory());
        });

        CompletableFuture.runAsync(()->{
            fridge = new FilterPillsDisplay();
            Platform.runLater(()->fridgePane.setContent(fridge));
            fridge.setMaxWidth(330);
            fridge.getChildren().addListener((ListChangeListener<Node>) change -> {
                //TODO FIX should precisely update only one cell
                ingredientsTable.setItems(filteredIngredients);
                addCol.setCellFactory(this.getBtnFactory());
            });
        }).thenRunAsync(()->{
            searchField.textProperty().addListener((o, oldVal, newVal) -> {
                filteredIngredients = FXCollections.observableList(allIngredients.stream()
                        .filter(s -> s.toLowerCase().contains(newVal.toLowerCase())).toList());
                //TODO FIX there should be way to bind tableView and output of filtering
                ingredientsTable.setItems(filteredIngredients);
                addCol.setCellFactory(this.getBtnFactory());
            });
        }).thenRunAsync(() -> {
            loadingCircles = initLoadingCircles();
        });
    }

    @FXML
    public void search(ActionEvent actionEvent) {
        List<String> ingredients = fridge.getPills();
        if (ingredients.isEmpty()){
            LOG.info("fridge is empty!");
            return;
        }
        CompletableFuture.runAsync(()-> {
            loadingCircles.setVisible(true);
            Set<String> ids = new SearchEngineImpl().getIDsByIngredients(ingredients.toArray(String[]::new));
            LOG.info("found " + ids.size() + " meals: " + ids);
            List<Meal> meals = new MealGetterImpl().getMealsByIds(ids);
            EventBusFactory.getEventBus().post(new ShowMealsEvent(meals, View.RECOMMENDED));
            loadingCircles.setVisible(false);
        });
    }

    private LoadingCircles initLoadingCircles(){
        double radius = 70;
        int amountOfCircles = 8;
        double circlesRadius = Math.PI*radius/amountOfCircles-6;
        LoadingCircles circles = new LoadingCircles(radius, amountOfCircles, circlesRadius,
                2000, 4000);
        circles.setLayoutX(180-radius-circlesRadius);
        circles.setLayoutY(270);
        mainPane.getChildren().add(circles);
        circles.play();
        circles.setVisible(false);
        return circles;
    }

    private Callback<TableColumn<String, Void>, TableCell<String, Void>> getBtnFactory() {
        return new Callback<>() {
            @Override
            public TableCell<String, Void> call(final TableColumn<String, Void> param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            CompletableFuture.runAsync(() -> {
                            String ingredientName = getTableView().getItems().get(getIndex());
                            boolean inFridge = fridge.getPills().contains(ingredientName);
                            Button btn = new Button();
                            btn.setStyle("-fx-font-size: 12");
                            btn.setPrefWidth(100);
                            if (inFridge){
                                setRemoveFunc(btn);
                            }else {
                                setAddFunc(btn);
                            }
                            Platform.runLater(()->setGraphic(btn));
                            });
                        }
                    }

                    private void setAddFunc(Button btn){
                        CompletableFuture.runAsync(()-> {
                            ImageView imageView = new ImageView(new Image(System.getProperty("user.dir") +
                                    "/src/main/resources/org/zdulski/finalproject/icons/add-icon.png"));
                            imageView.setFitWidth(16);
                            imageView.setFitHeight(16);
                            Platform.runLater(()-> {
                                btn.setText("Add");
                                btn.setGraphic(imageView);
                            });
                            String ingredientName = getTableView().getItems().get(getIndex());
                            btn.setOnAction(e -> {
                                //System.out.println("ingredient: '" + ingredientName +"' added to fridge");
                                fridge.addPill(ingredientName);
                                //System.out.println(Arrays.toString(fridge.getPills().toArray(String[]::new)));
                                setRemoveFunc(btn);
                            });
                        });
                    }

                    private void setRemoveFunc(Button btn){
                        CompletableFuture.runAsync(()-> {
                            ImageView imageView = new ImageView(new Image(System.getProperty("user.dir") +
                                    "/src/main/resources/org/zdulski/finalproject/icons/backspace.png"));
                            imageView.setFitWidth(16);
                            imageView.setFitHeight(16);
                            Platform.runLater(()-> {
                                btn.setText("Remove");
                                btn.setGraphic(imageView);
                            });
                            String ingredientName = getTableView().getItems().get(getIndex());
                            btn.setOnAction(e -> {
                                //System.out.println("ingredient: '" + ingredientName +"' removed from fridge");
                                fridge.removePill(ingredientName);
                                //System.out.println(Arrays.toString(fridge.getPills().toArray(String[]::new)));
                                setAddFunc(btn);
                            });
                        });
                    }
                };
            }
        };
    }
}
