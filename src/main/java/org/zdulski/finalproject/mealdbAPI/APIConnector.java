package org.zdulski.finalproject.mealdbAPI;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class APIConnector {

    public static URLConnection getConnection(String url) throws IOException {
        URLConnection connection = new URL(url).openConnection();
//        connection.setRequestProperty("X-RapidAPI-Host", PropertyManager.getInstance().getProperty("RapidApiHost"));
//        connection.setRequestProperty("X-RapidAPI-Key", PropertyManager.getInstance().getProperty("RapidApiKey"));
        return connection;
    }

    public static InputStream getInputStream(String url) throws IOException {
        return getConnection(url).getInputStream();
    }
}
