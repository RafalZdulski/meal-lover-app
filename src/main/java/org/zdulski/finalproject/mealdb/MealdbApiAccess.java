package org.zdulski.finalproject.mealdb;

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
import java.util.List;

public class MealdbApiAccess {
    //TODO RETHINK shouldn't this stored differently, more safely?
    private final String rapidApiHost = "themealdb.p.rapidapi.com";
    private final String rapidApiKey = "826b487458msh6a6a565581ebf5bp1334dajsn530065f6de6d";

    private final String randomURL = "https://themealdb.p.rapidapi.com/random.php";
    private final String searchURL = "https://themealdb.p.rapidapi.com/search.php?";
    private final String listURL = "https://themealdb.p.rapidapi.com/list.php?";

    public Meal getRandomMeal(){
        Meal ret = null;
        try {
            InputStream is = newConnection(randomURL).getInputStream();
            JSONParser jsonParser = new JSONParser();
            //TODO secure if it returns null? parse exception?
            JSONObject jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            System.out.println("random:\n"+jsonObject);
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            ret = new Meal((JSONObject) jsonArray.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            return ret;
        }
    }

    public List<Meal> getMealsByFirstLetter(char letter){
        List<Meal> ret = new ArrayList<>();
        try {
            InputStream is = newConnection(searchURL+"f="+letter).getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            //System.out.println("search:\n"+jsonObject);
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            for (var meal : jsonArray)
                 ret.add(new Meal((JSONObject) meal));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            return ret;
        }
    }

    public List<String> getCategories(){
        List<String> ret = new ArrayList<>();
        try {
            InputStream is = newConnection(listURL+"c=list").getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            System.out.println("search:\n"+jsonObject);
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            for (var category : jsonArray) {
                JSONObject temp = (JSONObject) category;
                ret.add((String) temp.get("strCategory"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return ret;
        }
    }

    public List<String> getAreas(){
        List<String> ret = new ArrayList<>();
        try {
            InputStream is = newConnection(listURL+"a=list").getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            System.out.println("search:\n"+jsonObject);
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            for (var category : jsonArray) {
                JSONObject temp = (JSONObject) category;
                ret.add((String) temp.get("strArea"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return ret;
        }
    }


    private URLConnection newConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("X-RapidAPI-Host", rapidApiHost);
        connection.setRequestProperty("X-RapidAPI-Key", rapidApiKey);
        return connection;
    }
}
