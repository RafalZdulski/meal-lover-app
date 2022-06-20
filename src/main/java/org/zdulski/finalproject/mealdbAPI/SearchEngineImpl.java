package org.zdulski.finalproject.mealdbAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.zdulski.finalproject.config.PropertyManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SearchEngineImpl implements SearchEngine{
    private static final Logger LOG = LogManager.getLogger(SearchEngineImpl.class);

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
        for (String category : categories){
            String url = PropertyManager.getInstance().getProperty("filterURL")+"?c="+category;
            try {
                ids.addAll(getIds(url));
            } catch (IOException e) {
                LOG.error("IOException in getIDsByName(): " + e.getMessage() + "\n" +
                        "failed at url: " + url);
                e.printStackTrace();
            }
        }
        return ids;
    }

    public Set<String> getIDsByArea(String... areas){
        Set<String> ids = new HashSet<>();
        for (String area : areas){
            String url = PropertyManager.getInstance().getProperty("filterURL")+"?a="+area;
            try {
                ids.addAll(getIds(url));
            } catch (IOException e) {
                LOG.error("IOException in getIDsByName(): " + e.getMessage() + "\n" +
                        "failed at url: " + url);
                e.printStackTrace();
            }
        }
        return ids;
    }

    public Set<String> getIDsByName(String... words){
        Set<String> ids = new HashSet<>();
        for (String name : words){
            String url = PropertyManager.getInstance().getProperty("searchURL")+"?s="+name;
            try {
                ids.addAll(getIds(url));
            } catch (IOException e) {
                LOG.error("IOException in getIDsByName(): " + e.getMessage() + "\n" +
                        "failed at url: " + url);
                e.printStackTrace();
            }
        }
        return ids;
    }

    @Override
    public Set<String> getIDsByIngredients(String... ingredients) {
        List<List<String>> subset = new ArrayList<>();
        List<String> input = Arrays.asList(ingredients);
        findSubsets(subset, input, new ArrayList<>(), 0);
        Collections.sort(subset, (o1, o2) -> o2.size() - o1.size());

        Set<String> ids = new HashSet<>();
        for (var ingredientsSubset : subset){
            ids.addAll(this.getIDsByAllIngredients(ingredientsSubset.toArray(String[]::new)));
        }

        return ids;
    }

    @Override
    public Set<String> getIDsByAllIngredients(String... ingredients) {
        Set<String> ids = new HashSet<>();
        if (ingredients.length < 1)
            return ids;
        String ingredientJoin = Arrays.stream(ingredients).collect(Collectors.joining(","));
        String url = PropertyManager.getInstance().getProperty("filterURL")+"?i="+ingredientJoin;
        try {
            LOG.info("getting IDs by ingredients: '" + ingredientJoin + "'");
            ids.addAll(getIds(url));
        } catch (IOException e) {
            LOG.error("IOException in getIDsByName(): " + e.getMessage() + "\n" + "failed at url: " + url);
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public Set<String> getIDsByEachIngredient(String... ingredients) {
        Set<String> ids = new HashSet<>();
        for (String ingredient : ingredients){
            String url = PropertyManager.getInstance().getProperty("filterURL")+"?i="+ingredient;
            try {
                LOG.info("getting IDs by ingredient: '" + ingredient + "'");
                ids.addAll(getIds(url));
            } catch (IOException e) {
                LOG.error("IOException in getIDsByName(): " + e.getMessage() + "\n" + "failed at url: " + url);
                e.printStackTrace();
            }
        }
        return ids;
    }

    private List<String> getIds(String url) throws IOException {
        //TODO IMPROVE decoding to url standard shouldn't be done by hand, check: org.apache.commons.codec.net.URLCodec
        url = url.replaceAll(",","%2C").replaceAll(" ","%20");
        InputStream is = APIConnector.getInputStream(url);
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray jsonArray = (JSONArray) jsonObject.get("meals");
            if (jsonArray == null || jsonArray.isEmpty())
                return new ArrayList<>();
            return (List<String>) jsonArray.stream().map(a -> String.valueOf(JSONObject.class.cast(a).get("idMeal"))).collect(Collectors.toList());
        } catch (ParseException e) {
            LOG.error("could not parse data from: " + url + " reason: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void findSubsets(List<List<String>> subset, List<String> nums, List<String> output, int index) {
        // Base Condition
        if (index == nums.size()) {
            subset.add(output);
            return;
        }

        // Not Including Value which is at Index
        findSubsets(subset, nums, new ArrayList<>(output), index + 1);

        // Including Value which is at Index
        output.add(nums.get(index));
        findSubsets(subset, nums, new ArrayList<>(output), index + 1);
    }
}
