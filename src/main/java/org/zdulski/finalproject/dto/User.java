package org.zdulski.finalproject.dto;

import java.util.HashSet;
import java.util.Set;

public class User {

    private long id;

    private String username;

    private String mail;

    private Set<String> favouriteMeals;


    public User(String username){
        this.username = username;
        favouriteMeals = new HashSet<>();
    }

    public String getUsername() {
        return username;
    }

    public Set<String> getFavouriteMeals() {
        return favouriteMeals;
    }
}
