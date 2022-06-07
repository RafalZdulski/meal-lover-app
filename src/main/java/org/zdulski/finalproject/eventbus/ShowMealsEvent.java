package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.dto.Meal;

import java.util.List;

public class ShowMealsEvent {

    private List<Meal> meals;

    public ShowMealsEvent(List<Meal> meals) {
        this.meals = meals;
    }

    public List<Meal> getMeals() {
        return meals;
    }
}
