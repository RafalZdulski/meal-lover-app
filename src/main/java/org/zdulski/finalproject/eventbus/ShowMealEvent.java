package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.data.dto.Meal;
import org.zdulski.finalproject.view_controllers.MealController;
import org.zdulski.finalproject.view_controllers.View;

public class ShowMealEvent {

    private Meal meal;

    private MealController.Action rightBtnAction;

    private View sourceView;

    public ShowMealEvent(Meal meal, MealController.Action rightBtnAction, View sourceView){
        this.meal = meal;
        this.rightBtnAction = rightBtnAction;
        this.sourceView = sourceView;
    }

    public Meal getMeal() {
        return this.meal;
    }

    public MealController.Action getAction(){
        return this.rightBtnAction;
    }

    public View getSourceView(){
        return this.sourceView;
    }
}
