package org.zdulski.finalproject.data.dto;

import com.google.common.eventbus.Subscribe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdulski.finalproject.data.repository.FavMealRepository;
import org.zdulski.finalproject.data.repository.NoSuchUserException;
import org.zdulski.finalproject.data.repository.UserRepositoryImpl;
import org.zdulski.finalproject.eventbus.AddToFavouriteEvent;
import org.zdulski.finalproject.eventbus.EventBusFactory;
import org.zdulski.finalproject.eventbus.ShowMealEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * handle access to the currently logged-in user
 * */
public class UserProxy {
    private static final Logger LOG = LogManager.getLogger(UserProxy.class);

    private static final UserProxy INSTANCE = new UserProxy();
    public static UserProxy getInstance(){return  INSTANCE;}

    private User user;

    private List<String> latestMeals;

    private FavMealRepository mealRepo;

    private UserProxy(){
        EventBusFactory.getEventBus().register(this);
        latestMeals = new ArrayList<>();
        this.mealRepo = new UserRepositoryImpl();
    }

    public boolean isFavourite(Meal meal){
        return user.getFavouriteMeals().contains(meal.getId());
    }

    public boolean isFavourite(String id){
        return user.getFavouriteMeals().contains(id);
    }

    public void setUser(User user){
        this.user = user;
        this.latestMeals = new ArrayList<>();
    }

    @Subscribe
    public void addToFavourite(AddToFavouriteEvent event){
        if (!isFavourite(event.getMeal().getId())) {
            user.getFavouriteMeals().add(event.getMeal().getId());
            LOG.info("meal: '" + event.getMeal().getName() + "' added to favourite of user: '" + user.getUsername() + "'");
            try {
                mealRepo.add(user.getUsername(), event.getMeal().getId());
            } catch (NoSuchUserException e) {
                //can only occur if user was deleted while the program was running
                LOG.fatal("could not find user: '" + user.getUsername() + "' in database - probably deleted during app runtime");
                e.printStackTrace();
            }
        }else {
            user.getFavouriteMeals().remove(event.getMeal().getId());
            LOG.info("meal: '" + event.getMeal().getName() + "' removed from favourite of user: '" + user.getUsername() + "'");
            mealRepo.delete(user.getUsername(), event.getMeal().getId());
        }
    }

    @Subscribe
    public void addToLatest(ShowMealEvent event){
        LOG.info("meal: '" + event.getMeal().getName() + "' added to latest of user: '" + user.getUsername() + "'");
        this.addToLatest(event.getMeal().getId());
    }

    private void addToLatest(String id){
        latestMeals.remove(id);
        latestMeals.add(0, id);
    }

    public Set<String> getFavourites() {
        return this.user.getFavouriteMeals();
    }

    public List<String> getLatest() {
        return this.latestMeals;
    }

    public String getUsername(){return this.user.getUsername();}
}
