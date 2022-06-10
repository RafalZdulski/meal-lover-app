package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.data.dto.Meal;

public class ShowMealEvent {

    private Meal meal;

    public ShowMealEvent(Meal meal){
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }
}
