package org.zdulski.finalproject.view_auxs;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealEvent;
import org.zdulski.finalproject.view_controllers.MealBrowseViewController;

import java.io.IOException;

public class MealCellFactory implements Callback<ListView<Meal>, ListCell<Meal>> {
    //TODO OPTIMIZE IT!!!!

    @Override
    public ListCell<Meal> call(ListView<Meal> mealListView) {
        return new ListCell<>(){
            @Override
            public void updateItem(Meal meal, boolean empty){
                super.updateItem(meal,empty);
                if (!empty){
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
        EventBusFactory.getEventBus().post(new ShowMealEvent(meal));
    }
}
