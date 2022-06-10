package org.zdulski.finalproject.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class User {

    @Getter
    private long id;

    @Getter
    private String username;

    @Getter
    private String mail;

    @Getter
    private Set<String> favouriteMeals;


    public User(String username){
        this.username = username;
        favouriteMeals = new HashSet<>();
    }
}
