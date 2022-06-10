package org.zdulski.finalproject.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.zdulski.finalproject.data.dto.User;
import org.zdulski.finalproject.data.pojo.UserMapper;
import org.zdulski.finalproject.data.pojo.UserPojo;

public class UserRepositoryImpl implements UserRepository{
    @Override
    public void saveUser(User user) {

    }

    @Override
    public User getUser(String name) {
        return null;
    }

    @Override
    public User getUser(long id) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }

    public static void main(String... args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();

        User user = new User("default");
        em.persist(UserMapper.toUserPojo(user));

        em.find(UserPojo.class, user);
    }

}
