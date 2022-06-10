package org.zdulski.finalproject.data.pojo;

import org.zdulski.finalproject.data.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserPojo toUserPojo(User user){
        return new UserPojo(user.getUsername(), user.getMail());
    }

    public static List<FavouriteMealPojo> getFavMealPojo(User user){
        List<FavouriteMealPojo> ret = new ArrayList<>();
        for (String id : user.getFavouriteMeals())
            ret.add(new FavouriteMealPojo(id, user.getId()));
        return null;
    }

    public static User toUser(UserPojo userPojo, List<FavouriteMealPojo> favouriteMealPojoList){
        Set<String> mealsId = favouriteMealPojoList.stream()
                .map(FavouriteMealPojo::getMealId).collect(Collectors.toSet());
        return new User(userPojo.getUserId(), userPojo.getUsername(),
                userPojo.getMail(), mealsId);
    }
}
