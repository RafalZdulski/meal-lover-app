package org.zdulski.finalproject.view_auxs;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealEvent;

public class MealCellFactory implements Callback<ListView<Meal>, ListCell<Meal>> {

    @Override
    public ListCell<Meal> call(ListView<Meal> mealListView) {
        ListCell<Meal> cell = new ListCell<>(){
            @Override
            public void updateItem(Meal meal, boolean empty){
                super.updateItem(meal,empty);
                if (!empty){
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(5);
                    gridPane.setVgap(5);
                    //name
                    Text name = new Text(meal.getName());
                    name.setWrappingWidth(205);
                    name.setFont(Font.font("system", FontWeight.BOLD, 20));
                    gridPane.add(name,0,0);
                    GridPane.setColumnSpan(name,2);
                    //area
                    Text area = new Text(meal.getArea());
                    area.setWrappingWidth(100);
                    area.setTextAlignment(TextAlignment.CENTER);
                    area.setFont(Font.font(16));
                    gridPane.add(area,0,1);
                    //category
                    Text category = new Text(meal.getCategory());
                    category.setWrappingWidth(100);
                    category.setFont(Font.font(16));
                    gridPane.add(category,1,1);
                    //Tags //TODO MAKE TAGS USEFUL
                    Text tags = new Text(meal.getTags());
                    tags.setFont(Font.font(12));
                    tags.setTextAlignment(TextAlignment.CENTER);
                    tags.setWrappingWidth(205);
                    gridPane.add(tags,0,2);
                    GridPane.setColumnSpan(tags,2);
                    //Photo
                    ImageView imageView = new ImageView();
                    imageView.setFitWidth(120);
                    imageView.setFitHeight(120);
                    imageView.setImage(new Image(meal.getThumbnail()));
                    gridPane.add(imageView,2, 0);
                    GridPane.setRowSpan(imageView, 3);

                    this.setGraphic(gridPane);
                }
            }
        };

        cell.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            System.out.println("clicked on: " + cell.getItem().getName());
            EventBusFactory.getEventBus().post(new ShowMealEvent(cell.getItem()));
        });

        return cell;
    }
}
