package org.zdulski.finalproject.data.pojos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserPojo {

    @Getter
    @Setter
    @Id
    private String username;

    @Getter
    @Setter
    private String mail;

    @Override
    public String toString() {
        String builder = "name: " + username + "\n" +
                "mail: " + mail + "\n";
        return builder;
    }
}
