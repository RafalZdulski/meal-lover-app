package org.zdulski.finalproject.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class User {

    @Getter
    private final String username;

    @Getter
    @Setter
    private String mail;

    @Getter
    private Set<String> favouriteMeals;


    public User(String username){
        this.username = username;
        this.favouriteMeals = new HashSet<>();
    }

    public User(String username, String mail) {
        this.username = username;
        this.mail = mail;
        this.favouriteMeals = new HashSet<>();
    }

    @Override
    public String toString(){
        String favMeals = String.join(", ", favouriteMeals);
        String builder = "name: " + username + ", " +
                "mail: " + mail + ", " +
                "fav meals: " + favMeals;
        return builder;
    }
}
