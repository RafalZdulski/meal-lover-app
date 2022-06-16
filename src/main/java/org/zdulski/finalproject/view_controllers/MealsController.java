package org.zdulski.finalproject.view_controllers;

import org.zdulski.finalproject.data.dto.Meal;

import java.util.Collection;
import java.util.List;

public interface MealsController extends ViewController{
    void setMeals(List<Meal> meals);

    void setMealsByIds(Collection<String> ids);
}
