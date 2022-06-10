package org.zdulski.finalproject.mealdbAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zdulski.finalproject.config.PropertyManager;
import org.zdulski.finalproject.data.dto.Meal;

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

    private URLConnection newConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("X-RapidAPI-Host", PropertyManager.getInstance().getProperty("RapidApiHost"));
        connection.setRequestProperty("X-RapidAPI-Key", PropertyManager.getInstance().getProperty("RapidApiKey"));
        return connection;
    }

    public List<Meal> getMealsByIds(Collection<String> ids){
        List<Meal> meals = new ArrayList<>();
        for (String id : ids)
            meals.add(getMealById(id));
        return meals;
    }

    public Meal getMealById(String id){
        String url = PropertyManager.getInstance().getProperty("lookupURL")+"?i="+id;
        return getMeals(url).get(0);
    }

    public Meal getRandomMeal(){
        String url = PropertyManager.getInstance().getProperty("randomURL");
        return getMeals(url).get(0);
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
        String url = PropertyManager.getInstance().getProperty("searchURL")+"?f="+letter;
        return getMeals(url);
    }

    public List<String> getCategories(){
        List<String> ret = new ArrayList<>();
        try {
            String url = PropertyManager.getInstance().getProperty("listURL")+"?c=list";
            InputStream is = newConnection(url).getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            for (Object category : jsonArray) {
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
            String url = PropertyManager.getInstance().getProperty("listURL")+"?a=list";
            InputStream is = newConnection(url).getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            for (Object category : jsonArray) {
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
                for (Object meal : jsonArray)
                    ret.add(new Meal((JSONObject) meal));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ret;
    }

}
