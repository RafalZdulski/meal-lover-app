package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.dto.Meal;
import org.zdulski.finalproject.view_controllers.View;

import java.util.List;

public class ShowMealsEvent {

    private List<Meal> meals;

    private View viewType;

    public ShowMealsEvent(List<Meal> meals, View viewType) {
        this.meals = meals;
        this.viewType = viewType;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public View getViewType(){
        return viewType;
    }
}
