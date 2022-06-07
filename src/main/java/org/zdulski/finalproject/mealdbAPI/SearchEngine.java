package org.zdulski.finalproject.mealdbAPI;

import java.util.Set;

public interface SearchEngine {

    Set<String> getIDs(String[] wordsInName, String[] areas, String[] categories);

    Set<String> getIDsByCategory(String... categories);

    Set<String> getIDsByArea(String... areas);

    Set<String> getIDsByName(String... words);

}
