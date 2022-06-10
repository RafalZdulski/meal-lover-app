package org.zdulski.finalproject.repository;

import org.zdulski.finalproject.data.dto.User;

public interface UserRepository {
    void saveUser(User user);
    User getUser(String name);
    User getUser(long id);
    void deleteUser(User user);
}
