package org.zdulski.finalproject.data.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

//@Table(name = "Users")
@Entity
@NoArgsConstructor
public class UserPojo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    private long id;

    @Getter
    @Column(nullable = false, unique = true)
    private String username;

    @Getter
    private String mail;

    public UserPojo(String username, String mail) {
        this.username = username;
        this.mail = mail;
    }

    public UserPojo(@NonNull String username) {
        this.username = username;
    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("id: ").append(id).append("\n")
                .append("name: ").append(username).append("\n")
                .append("mail: ").append(mail).append("\n");
        return builder.toString();
    }
}
