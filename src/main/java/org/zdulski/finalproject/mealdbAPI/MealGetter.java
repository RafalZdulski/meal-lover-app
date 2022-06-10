package org.zdulski.finalproject.mealdbAPI;

import org.zdulski.finalproject.data.dto.Meal;

import java.util.Collection;
import java.util.List;

public interface MealGetter {

    List<Meal> getMealsByIds(Collection<String> ids);

    Meal getMealById(String id);

    Meal getRandomMeal();

    List<Meal> getAllMeals();

    List<Meal> getMealsByFirstLetter(char letter);

    List<String> getCategories();

    List<String> getAreas();
}
