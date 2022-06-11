package org.zdulski.finalproject.data.dto;

import com.google.common.eventbus.Subscribe;
import org.zdulski.finalproject.eventbus.AddToFavouriteEvent;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealEvent;
import org.zdulski.finalproject.data.repository.UserRepository;
import org.zdulski.finalproject.data.repository.UserRepositoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UserProxy {
    private static UserProxy INSTANCE = new UserProxy();
    public static UserProxy getInstance(){return  INSTANCE;}

    private User user;

    private List<String> latestMeals;

    private UserRepository repo;

    private UserProxy(){
        EventBusFactory.getEventBus().register(this);
        latestMeals = new ArrayList<>();
        this.repo = new UserRepositoryImpl();
    }

    public boolean isFavourite(Meal meal){
        return user.getFavouriteMeals().contains(meal.getId());
    }

    public boolean isFavourite(String id){
        return user.getFavouriteMeals().contains(id);
    }

    public void newUser(String name){
        User user = new User(name);

    }

    public void setUser(String name){

    }

    public void setUser(User user){
        this.user = user;
        this.latestMeals = new ArrayList<>();
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
        this.addToLatest(event.getMeal().getId());
    }

    private void addToLatest(String id){
        latestMeals.remove(id);
        latestMeals.add(0, id);
    }

    public Set<String> getFavourites() {
        return user.getFavouriteMeals();
    }

    public List<String> getLatest() {
        return latestMeals;
    }
}
