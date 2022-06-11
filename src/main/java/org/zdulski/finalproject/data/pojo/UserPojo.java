package org.zdulski.finalproject.data.pojo;

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
        StringBuilder builder = new StringBuilder();
        builder.append("name: ").append(username).append("\n")
                .append("mail: ").append(mail).append("\n");
        return builder.toString();
    }
}
