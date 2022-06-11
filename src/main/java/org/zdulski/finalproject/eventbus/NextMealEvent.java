package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.view_controllers.View;

public class NextMealEvent {
    Meal currentMeal;
    View sourceView;

    public NextMealEvent(Meal currentMeal, View sourceView){
        this.currentMeal = currentMeal;
        this.sourceView = sourceView;
    }

    public Meal getMeal(){
        return this.currentMeal;
    }

    public View getSourceView() {
        return this.sourceView;
    }
}
