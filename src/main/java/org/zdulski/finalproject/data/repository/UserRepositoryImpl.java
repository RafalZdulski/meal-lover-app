package org.zdulski.finalproject.data.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import org.zdulski.finalproject.data.dto.User;
import org.zdulski.finalproject.data.pojo.FavouriteMealPojo;
import org.zdulski.finalproject.data.pojo.UserPojo;
import org.zdulski.finalproject.data.pojo.UserPojoMapper;

import java.util.List;

public class UserRepositoryImpl implements UserRepository{
    EntityManagerFactory emf;

    public UserRepositoryImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    public UserRepositoryImpl(){
        this.emf = Persistence.createEntityManagerFactory("default");
    }

    @Override
    public User newUser(String username, String mail) {
        User user = new User(username, mail);
        this.saveUser(user);
        return user;
    }

    @Override
    public User newUser(String username) {
        User user = new User(username);
        this.saveUser(user);
        return user;
    }

    @Override
    public void saveUser(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        UserPojo userPojo = UserPojoMapper.toUserPojo(user);
        em.persist(userPojo);

        for (var favMealPojo : UserPojoMapper.getFavMealPojo(user))
            em.persist(favMealPojo);

        try {
            em.getTransaction().commit();
        }catch (RollbackException e){
            //probably user with such username already exists
            System.err.println(e.getMessage());
            System.err.println("!changes not committed!");
        }
        em.close();
    }

    @Override
    public User getUser(String username) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
//        UserPojo userPojo = em.createQuery("SELECT u FROM UserPojo u WHERE u.username = :name", UserPojo.class)
//                .setParameter("username", username).getSingleResult();
        UserPojo userPojo = em.find(UserPojo.class, username);
        if (userPojo == null) {
            em.close();
            //throw new IllegalStateException("could not find user: " + username);
            return null;
        }

        List<FavouriteMealPojo> mealPojoList = em.createQuery("SELECT m FROM FavouriteMealPojo m WHERE m.username = :username", FavouriteMealPojo.class)
                .setParameter("username", userPojo.getUsername()).getResultList();

        em.close();
        return UserPojoMapper.toUser(userPojo, mealPojoList);
    }

    @Override
    public void deleteUser(User user) {
        EntityManager em = emf.createEntityManager();
        //TODO implement deleting user
        em.close();
    }




    public static void main(String... args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");

        User user = new User("default", "mail@gmail.com");

        user.getFavouriteMeals().add("54427");
        user.getFavouriteMeals().add("32212");

        System.out.println(user);
        System.out.println(UserPojoMapper.toUserPojo(user));
        System.out.println(UserPojoMapper.getFavMealPojo(user));

        new UserRepositoryImpl(emf).saveUser(user);
        new UserRepositoryImpl(emf).saveUser(user);

        System.out.println("\n----------------------------------------------------\n");

        User found = new UserRepositoryImpl(emf).getUser("default");

        new UserRepositoryImpl(emf).getUser("dontExists");

        System.out.println(found);

    }

}
