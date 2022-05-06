package org.zdulski.finalproject.mealdb;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zdulski.finalproject.mealdb.dto.Meal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ApiController {
    public Meal getRandomMeal(){
        Meal ret = null;
        URLConnection connection = null;
        try {
            connection = new URL("https://themealdb.p.rapidapi.com/random.php").openConnection();
            //TODO RETHINK shouldn't this stored differently, more safely?
            connection.setRequestProperty("X-RapidAPI-Host", "themealdb.p.rapidapi.com");
            connection.setRequestProperty("X-RapidAPI-Key", "826b487458msh6a6a565581ebf5bp1334dajsn530065f6de6d");

            //Get Response
            InputStream is = connection.getInputStream();
            JSONParser jsonParser = new JSONParser();
            //TODO secure if it returns null? parse exception?
            JSONObject jsonObject = (JSONObject)jsonParser.parse(
                    new InputStreamReader(is, StandardCharsets.UTF_8));
            System.out.println("\n"+jsonObject);

            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            //System.out.println(jsonArray.get(0));
            ret = new Meal((JSONObject) jsonArray.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            return ret;
        }
    }
}
