package org.zdulski.finalproject.view_auxs;

import javafx.application.Platform;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.LoadingFinishedEvent;
import org.zdulski.finalproject.eventbus.ShowMealEvent;
import org.zdulski.finalproject.view_controllers.MealController;
import org.zdulski.finalproject.view_controllers.View;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MealCellFactory implements Callback<ListView<Meal>, ListCell<Meal>> {

    private final View sourceView;

    public MealCellFactory(View sourceView){
        this.sourceView = sourceView;
    }


    @Override
    public ListCell<Meal> call(ListView<Meal> mealListView) {
        CompletableFuture<ListCell<Meal>> future = CompletableFuture.supplyAsync(()-> {
            ListCell<Meal> cell = new ListCell<>() {
                public void updateItem(Meal meal, boolean empty) {
                    super.updateItem(meal, empty);
                    if (meal != null && !empty) {
                        GridPane gridPane = new GridPane();
                        gridPane.setHgap(5);
                        gridPane.setVgap(5);
                        //name
                        Text name = new Text(meal.getName());
                        name.setWrappingWidth(230);
                        if(meal.getName().length() < 40)
                            name.setFont(Font.font("system", FontWeight.BOLD, 20));
                        else
                            name.setFont(Font.font("system", FontWeight.BOLD, 16));
                        gridPane.add(name, 0, 0);
                        GridPane.setColumnSpan(name, 2);
                        //area
                        Text area = new Text(meal.getArea());
                        area.setWrappingWidth(100);
                        area.setTextAlignment(TextAlignment.CENTER);
                        area.setFont(Font.font(16));
                        gridPane.add(area, 0, 1);
                        //category
                        Text category = new Text(meal.getCategory());
                        category.setWrappingWidth(100);
                        category.setFont(Font.font(16));
                        gridPane.add(category, 1, 1);
                        //Tags //TODO ADD make tags useful
//                        Text tags = new Text(meal.getTags());
//                        tags.setFont(Font.font(12));
//                        tags.setTextAlignment(TextAlignment.CENTER);
//                        tags.setWrappingWidth(205);
//                        gridPane.add(tags,0,2);
//                        GridPane.setColumnSpan(tags,2);
                        //Photo
                        Rectangle photo = new Rectangle(96, 96);
                        new Thread(() -> {
                            photo.setArcHeight(40);
                            photo.setArcWidth(40);
                            photo.setFill(new ImagePattern(new Image(meal.getThumbnail())));
                            //gridPane.add(photo, 2, 0);
                            Platform.runLater(() -> gridPane.add(photo, 2, 0));
                            GridPane.setRowSpan(photo, 3);
                        }).start();

                        Platform.runLater(() -> this.setGraphic(gridPane));

                    }
                }
            };
            return cell;
        }).thenApplyAsync(cell -> {
            if (cell != null) {
                cell.setPrefHeight(100);
                cell.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                    System.out.println("clicked on: " + cell.getItem().getName());
                    EventBusFactory.getEventBus().post(new ShowMealEvent(cell.getItem(), MealController.Action.NEXT_MEAL, sourceView));
                });
            }
            return cell;
        });
        try {
            ListCell<Meal> ret = future.get();
            EventBusFactory.getEventBus().post(new LoadingFinishedEvent());
            return ret;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
}
