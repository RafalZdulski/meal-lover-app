package org.zdulski.finalproject.view_auxs;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.zdulski.finalproject.mealdb.dto.Meal;
import org.zdulski.finalproject.mediator_controllers.ViewMediator;
import org.zdulski.finalproject.view_controllers.MealBrowseViewController;
import org.zdulski.finalproject.view_controllers.MealController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class MealCellFactory implements Callback<ListView<Meal>, ListCell<Meal>> {

    @Override
    public ListCell<Meal> call(ListView<Meal> mealListView) {
        return new ListCell<>(){
            @Override
            public void updateItem(Meal meal, boolean empty){
                super.updateItem(meal,empty);
                if (empty || meal == null) {
                    //setText("null or empty");
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/zdulski/finalproject/views/meal-browse-view.fxml"));
                        Pane pane = loader.load();
                        MealBrowseViewController controller = loader.getController();
                        controller.setMeal(meal);
                        setGraphic(pane);

                        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                            System.out.println("clicked on: " + meal.getName());
                            goToMealViewOf(meal);
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }


    public void goToMealViewOf(Meal meal){
        CompletableFuture<FXMLLoader> futurePane = CompletableFuture.supplyAsync(new Supplier<FXMLLoader>() {
            @Override
            public FXMLLoader get() {
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
                controller.setMeal(meal);
            });
            return loader;
        });
    }
}
