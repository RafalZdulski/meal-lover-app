package org.zdulski.finalproject.dto;

import lombok.Getter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Meal {
    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private String thumbnail;
    @Getter
    private String youtubeUrl;
    @Getter
    private String category;
    @Getter
    private String area;
    @Getter
    private String instructions;
    @Getter
    private Map<String, String> ingredients = new HashMap<>();
    @Getter
    private String tags;

    public Meal(JSONObject jsonObject) {
        //TODO secure in case some of values might be empty string or NULL
        //for now String.valueOf() solved problem but
        //fields that were NULL now contains "null"

        id = String.valueOf(jsonObject.get("idMeal"));
        name = String.valueOf(jsonObject.get("strMeal"));
        thumbnail = String.valueOf(jsonObject.get("strMealThumb"));
        youtubeUrl = String.valueOf(jsonObject.get("strYoutube"));
        category = String.valueOf(jsonObject.get("strCategory"));
        area = String.valueOf(jsonObject.get("strArea"));
        instructions = String.valueOf(jsonObject.get("strInstructions"));
        tags = String.valueOf(jsonObject.get("strTags")).replaceAll(",",", ");
        for (int i=1; i<=20; i++){
            String ingredient = String.valueOf(jsonObject.get("strIngredient"+i));
            String ingMeasure = String.valueOf(jsonObject.get("strMeasure"+i));
            ingredients.put(ingredient, ingMeasure);
        }
        ingredients.remove("");
        ingredients.remove("null");
    }

    //only for test purpose
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
                .append("ingredients: ").append(ingredients).append("\n");
        return builder.toString();
    }
}
