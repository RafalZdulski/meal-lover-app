package org.zdulski.finalproject.mealdbAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zdulski.finalproject.dto.Meal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MealGetterImpl implements MealGetter{
    //TODO RETHINK shouldn't this stored differently, more safely?
    private final String rapidApiHost = "themealdb.p.rapidapi.com";
    private final String rapidApiKey = "826b487458msh6a6a565581ebf5bp1334dajsn530065f6de6d";

    private final String randomURL = "https://themealdb.p.rapidapi.com/random.php";
    private final String searchURL = "https://themealdb.p.rapidapi.com/search.php";
    private final String listURL = "https://themealdb.p.rapidapi.com/list.php";
    private final String filterURL = "https://themealdb.p.rapidapi.com/filter.php";
    private final String lookupURL = "https://themealdb.p.rapidapi.com/lookup.php";

    private URLConnection newConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("X-RapidAPI-Host", rapidApiHost);
        connection.setRequestProperty("X-RapidAPI-Key", rapidApiKey);
        return connection;
    }

    public List<Meal> getMealsByIds(Collection<String> ids){
        List<Meal> meals = new ArrayList<>();
        for (var id : ids)
            meals.add(getMealById(id));
        return meals;
    }

    public Meal getMealById(String id){
        String url = lookupURL+"?i="+id;
        return getMeals(url).get(0);
    }

    public Meal getRandomMeal(){
        return getMeals(randomURL).get(0);
    }

    public List<Meal> getAllMeals(){
        List<Meal> allMeals = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch('z'-'a'+1);
        for (char letter = 'a'; letter <= 'z'; letter++){
            char finalLetter = letter;
            new Thread(() ->{
                allMeals.addAll(new MealGetterImpl().getMealsByFirstLetter(finalLetter));
                latch.countDown();
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return allMeals;
    }

    public List<Meal> getMealsByFirstLetter(char letter){
        String url = searchURL+"?f="+letter;
        return getMeals(url);
    }

    public List<String> getCategories(){
        List<String> ret = new ArrayList<>();
        try {
            InputStream is = newConnection(listURL+"?c=list").getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            for (var category : jsonArray) {
                JSONObject temp = (JSONObject) category;
                ret.add((String) temp.get("strCategory"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;

    }

    public List<String> getAreas(){
        List<String> ret = new ArrayList<>();
        try {
            InputStream is = newConnection(listURL+"?a=list").getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            for (var category : jsonArray) {
                JSONObject temp = (JSONObject) category;
                ret.add((String) temp.get("strArea"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;

    }

    private List<Meal> getMeals(String url){
        List<Meal> ret = new ArrayList<>();
        try {
            InputStream is = newConnection(url).getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            if (jsonArray != null)
                for (var meal : jsonArray)
                    ret.add(new Meal((JSONObject) meal));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
