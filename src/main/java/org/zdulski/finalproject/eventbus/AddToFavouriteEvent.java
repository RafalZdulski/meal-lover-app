package org.zdulski.finalproject.eventbus;

import org.zdulski.finalproject.dto.Meal;

public class AddToFavouriteEvent {
    private Meal meal;

    public AddToFavouriteEvent(Meal meal){
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }
}
