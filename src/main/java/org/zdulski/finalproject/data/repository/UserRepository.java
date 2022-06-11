package org.zdulski.finalproject.data.repository;

import org.zdulski.finalproject.data.dto.User;

public interface UserRepository {

    User newUser(String username, String mail) throws UserAlreadyExistsException;

    User newUser(String username) throws UserAlreadyExistsException;

    void saveUser(User user) throws UserAlreadyExistsException;

    User getUser(String username) throws NoSuchUserException;

    void deleteUser(User user);

    void updateUsersMail(User user, String newMail);

    boolean doesUserExist(String username);

}
