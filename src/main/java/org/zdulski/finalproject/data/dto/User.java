package org.zdulski.finalproject.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
        String favMeals = favouriteMeals.stream().collect(Collectors.joining(", "));
        StringBuilder builder = new StringBuilder();
        builder.append("name: ").append(username).append(", ")
                .append("mail: ").append(mail).append(", ")
                .append("fav meals: ").append(favMeals);
        return builder.toString();
    }
}
