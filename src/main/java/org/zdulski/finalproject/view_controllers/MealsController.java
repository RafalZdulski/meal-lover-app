package org.zdulski.finalproject.view_controllers;

import org.zdulski.finalproject.data.dto.Meal;

import java.util.List;

public interface MealsController extends ViewController{
    void setMeals(List<Meal> meals);
}
