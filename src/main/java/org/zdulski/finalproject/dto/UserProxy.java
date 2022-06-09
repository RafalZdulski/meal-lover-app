package org.zdulski.finalproject.dto;

import com.google.common.eventbus.Subscribe;
import org.zdulski.finalproject.eventbus.AddToFavouriteEvent;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealEvent;

import java.util.HashSet;
import java.util.Set;

public class UserProxy {
    private static UserProxy INSTANCE = new UserProxy();
    public static UserProxy getInstance(){return  INSTANCE;}

    private User user;

    private Set<String> latestMeals;

    private UserProxy(){
        EventBusFactory.getEventBus().register(this);
        latestMeals = new HashSet<>();
    }

    public boolean isFavourite(Meal meal){
        return user.getFavouriteMeals().contains(meal.getId());
    }

    public boolean isFavourite(String id){
        return user.getFavouriteMeals().contains(id);
    }

    public void setUser(User user) {
        this.user = user;
        latestMeals = new HashSet<>();
    }

    @Subscribe
    public void addToFavourite(AddToFavouriteEvent event){
        if (!isFavourite(event.getMeal().getId())) {
            user.getFavouriteMeals().add(event.getMeal().getId());
            System.out.println("meal: " + event.getMeal().getName() + " added to favourite of user: " + user.getUsername());
        }else {
            user.getFavouriteMeals().remove(event.getMeal().getId());
            System.out.println("meal: " + event.getMeal().getName() + " removed from favourite of user: " + user.getUsername());
        }
    }

    @Subscribe
    public void addToLatest(ShowMealEvent event){
        System.out.println("meal: " + event.getMeal().getName() + " added to latest of user: " + user.getUsername());
        addToLatest(event.getMeal().getId());
    }

    //TODO beautify it
    private void addToLatest(String id){
        latestMeals.add(id);
    }

    public Set<String> getFavourites() {
        return user.getFavouriteMeals();
    }

    public Set<String> getLatest() {
        return latestMeals;
    }
}
