package org.zdulski.finalproject.mealdbAPI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zdulski.finalproject.config.PropertyManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchEngineImpl implements SearchEngine{

    private URLConnection newConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("X-RapidAPI-Host", PropertyManager.getInstance().getProperty("RapidApiHost"));
        connection.setRequestProperty("X-RapidAPI-Key", PropertyManager.getInstance().getProperty("RapidApiKey"));
        return connection;
    }

    /**
     * filter by schema:
     * (word_1 OR ... OR word_N) AND (area_1 OR ... OR area_N) AND (category_1 OR ... OR category_N)
     */
    public Set<String> getIDs(String[] wordsInName, String[] areas, String[] categories) {
        Set<String> byName = null, byAreas = null, byCategories = null;

        int val = 0;

        if (wordsInName != null && wordsInName.length > 0) {
            byName = getIDsByName(wordsInName);
            val += 0b1;
        }
        if (areas != null && areas.length > 0){
            byAreas = getIDsByArea(areas);
            val += 0b10;
        }
        if (categories !=null && categories.length >0){
            byCategories = getIDsByCategory(categories);
            val += 0b100;
        }

        return switch (val) {
            case 0b001 -> byName;
            case 0b010 -> byAreas;
            case 0b011 -> byName.stream().distinct().filter(byAreas::contains).collect(Collectors.toSet());
            case 0b100 -> byCategories;
            case 0b101 -> byName.stream().distinct().filter(byCategories::contains).collect(Collectors.toSet());
            case 0b110 -> byAreas.stream().distinct().filter(byCategories::contains).collect(Collectors.toSet());
            case 0b111 -> byName.stream().distinct().filter(byAreas::contains).filter(byCategories::contains).collect(Collectors.toSet());
            default -> new HashSet<>();
        };
    }

    public Set<String> getIDsByCategory(String... categories){
        Set<String> ids = new HashSet<>();
        for (var category : categories){
            String url = PropertyManager.getInstance().getProperty("filterURL")+"?c="+category;
            ids.addAll(getIds(url));
        }
        return ids;
    }

    public Set<String> getIDsByArea(String... areas){
        Set<String> ids = new HashSet<>();
        for (var area : areas){
            String url = PropertyManager.getInstance().getProperty("filterURL")+"?a="+area;
            ids.addAll(getIds(url));
        }
        return ids;
    }

    public Set<String> getIDsByName(String... words){
        Set<String> ids = new HashSet<>();
        for (var name : words){
            String url = PropertyManager.getInstance().getProperty("searchURL")+"?s="+name;
            ids.addAll(getIds(url));
        }
        return ids;
    }

    private List<String> getIds(String url){
        try {
            InputStream is = newConnection(url).getInputStream();
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            return  jsonArray.stream().map(a -> String.valueOf(JSONObject.class.cast(a).get("idMeal"))).toList();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
