package org.zdulski.finalproject.data.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "favourite_meals")
public class FavouriteMealPojo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String mealId;

    @Getter
    @Setter
    private String username;

    public FavouriteMealPojo(String mealId, String username){
        this.mealId = mealId;
        this.username = username;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("user: ").append(username).append(", ")
                .append("meal_id: ").append(mealId).append("\n");
        return builder.toString();
    }

}
