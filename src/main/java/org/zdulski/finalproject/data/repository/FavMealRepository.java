package org.zdulski.finalproject.data.repository;

import java.util.Set;

public interface FavMealRepository {

    void delete(String username, String mealId);

    void add(String username, String mealId) throws NoSuchUserException;

    Set<String> getFavouriteMeals(String username);

    boolean isFavouriteMeal(String username, String mealId);
}
