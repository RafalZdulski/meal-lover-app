package org.zdulski.finalproject.data.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Table(name = "Favourite_Meals")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteMealPojo {

    @Id
    @Column(name = "meal_id")
    @Getter
    public String mealId;

    @Column(name = "user_id", nullable = false)
    @Getter
    public long userId;

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("user: ").append(userId).append("\n")
                .append("meal: ").append(mealId).append("\n");
        return builder.toString();
    }

}
