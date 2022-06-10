package org.zdulski.finalproject.data.pojo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

//@Table(name = "Favourite_Meals")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteMealPojo {

    @Id
    public String meal_id;

    @NonNull
    public long user_id;

    public String getMealId() {
        return meal_id;
    }

    public long getUserId() {
        return user_id;
    }
}
