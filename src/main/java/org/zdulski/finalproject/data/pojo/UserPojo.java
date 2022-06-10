package org.zdulski.finalproject.data.pojo;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.NonNull;

//@Table(name = "Users")
@Entity
@NoArgsConstructor
public class UserPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @NonNull
    private String username;

    private String mail;

    public UserPojo(String username, String mail) {
        this.username = username;
        this.mail = mail;
    }

    public UserPojo(@NonNull String username) {
        this.username = username;
    }

    public long getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }
}
