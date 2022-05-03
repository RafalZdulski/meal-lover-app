package org.zdulski.finalproject.mealdb.dto;

import org.json.simple.JSONObject;

public class Meal {
    private String id;
    private String name;
    private String thumbnail;
    private String youtubeUrl;

    private String category;
    private String area;

    private String instructions;

    private String[] ingredients = new String[20];
    private String[] ingredientMeasure = new String[20];

    private String tags;

    public Meal(JSONObject jsonObject) {
        //TODO solve some are empty string some are NULL
        //for now String.valueOf() solved problem but
        //fields that were NULL now contains "null"

        id = String.valueOf(jsonObject.get("idMeal"));
        name = String.valueOf(jsonObject.get("strMeal"));
        thumbnail = String.valueOf(jsonObject.get("strMealThumb"));
        youtubeUrl = String.valueOf(jsonObject.get("strYoutube"));
        category = String.valueOf(jsonObject.get("strCategory"));
        area = String.valueOf(jsonObject.get("strArea"));
        instructions = String.valueOf(jsonObject.get("strInstructions"));
        tags = String.valueOf(jsonObject.get("strTags"));
        for (int i=1; i<=20; i++){
            ingredients[i-1] = String.valueOf(jsonObject.get("strIngredient"+i));
            ingredientMeasure[i-1] = String.valueOf(jsonObject.get("strMeasure"+i));
        }
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("id: ").append(id).append("\n")
                .append("name: ").append(name).append("\n")
                .append("thumbnail: ").append(thumbnail).append("\n")
                .append("youtubeUrl: ").append(youtubeUrl).append("\n")
                .append("category: ").append(category).append("\n")
                .append("area: ").append(area).append("\n")
                .append("instructions:\n").append(instructions).append("\n")
                .append("tags: ").append(tags).append("\n")
                .append("ingredients:[ ").append("\n");
        for (int i=0; i<20; i++){
            builder.append("\t"+ingredients[i]+":"+ingredientMeasure[i]+",\n");
        }
        return builder.toString();
    }
}
