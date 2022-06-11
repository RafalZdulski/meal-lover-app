package org.zdulski.finalproject.data.repository;

import org.zdulski.finalproject.data.dto.User;

public interface UserRepository {

    User newUser(String username, String mail);

    User newUser(String username);

    void saveUser(User user);

    User getUser(String username);

    void deleteUser(User user);



}
