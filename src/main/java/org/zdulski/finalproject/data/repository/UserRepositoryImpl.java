package org.zdulski.finalproject.data.repository;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zdulski.finalproject.MainApplication;
import org.zdulski.finalproject.data.dto.User;
import org.zdulski.finalproject.data.pojos.FavouriteMealPojo;
import org.zdulski.finalproject.data.pojos.UserPojo;
import org.zdulski.finalproject.data.pojos.UserPojoMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository, FavMealRepository{
    private static final Logger LOG = LogManager.getLogger(UserRepositoryImpl.class);

    private EntityManagerFactory emf;

    public UserRepositoryImpl(EntityManagerFactory emf){
        this.emf = emf;
    }

    public UserRepositoryImpl(){
        this.emf = MainApplication.getEntityManagerFactory();
    }

    @Override
    public User newUser(String username, String mail) throws UserAlreadyExistsException {
        User user = new User(username, mail);
        this.saveUser(user);
        return user;
    }

    @Override
    public User newUser(String username) throws UserAlreadyExistsException {
        User user = new User(username);
        this.saveUser(user);
        return user;
    }

    @Override
    public void saveUser(User user) throws UserAlreadyExistsException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        UserPojo userPojo = UserPojoMapper.toUserPojo(user);
        em.persist(userPojo);

        for (var favMealPojo : UserPojoMapper.getFavMealPojo(user))
            em.persist(favMealPojo);

        try {
            em.getTransaction().commit();
            LOG.info("user: '" + user.getUsername() + "' saved into database");
        }catch (RollbackException e){
            if (this.doesUserExist(user.getUsername())) {
                LOG.error("user: '" + user.getUsername() + "' already exists in database");
                throw new UserAlreadyExistsException();
            }else
                e.printStackTrace();
        }finally {
            em.close();
        }
    }

    @Override
    public User getUser(String username) throws NoSuchUserException {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        UserPojo userPojo = em.find(UserPojo.class, username);
        if (userPojo == null) {
            em.close();
            LOG.error("user: '" + username + "' not found in database");
            throw new NoSuchUserException();
        }

        List<FavouriteMealPojo> mealPojoList = em.createQuery("SELECT m FROM FavouriteMealPojo m WHERE m.username = :username", FavouriteMealPojo.class)
                .setParameter("username", userPojo.getUsername()).getResultList();

        em.close();

        return UserPojoMapper.toUser(userPojo, mealPojoList);
    }

    @Override
    public void deleteUser(User user) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM UserPojo u WHERE u.username = :username")
                .setParameter("username", user.getUsername())
                .executeUpdate();

        em.createQuery("DELETE FROM FavouriteMealPojo m WHERE  m.username = :username")
                .setParameter("username", user.getUsername())
                .executeUpdate();

        em.getTransaction().commit();
        LOG.info("user: '" + user.getUsername() + "' deleted from database");
        em.close();
    }


    @Override
    public void updateUsersMail(User user, String newMail) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("UPDATE UserPojo SET mail = :newMail WHERE u.username = :username")
                .setParameter("newMail", newMail)
                .setParameter("username", user.getUsername())
                .executeUpdate();

        em.getTransaction().commit();
        LOG.info("user: '" + user.getUsername() + "' mail updated");
        em.close();
    }

    @Override
    public boolean doesUserExist(String username){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        boolean ret;
        var user = em.find(UserPojo.class, username);
        if (em.find(UserPojo.class, username) != null)
            ret = true;
        else
            ret = false;

        em.close();
        return ret;
    }

    @Override
    public void delete(String username, String mealId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("DELETE FROM FavouriteMealPojo m WHERE  m.username = :username AND m.mealId = :mealId")
                .setParameter("username", username)
                .setParameter("mealId", mealId)
                .executeUpdate();

        em.getTransaction().commit();
        LOG.info("user's: '" + username + "' favourite meal: '" + mealId + "'  deleted from database");
        em.close();
    }

    @Override
    public void add(String username, String mealId) throws NoSuchUserException {
        if (!doesUserExist(username))
            throw new NoSuchUserException();
        if (isFavouriteMeal(username, mealId))
            return;

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        em.persist(new FavouriteMealPojo(mealId, username));

        em.getTransaction().commit();
        LOG.info("user's: '" + username + "' favourite meal: '" + mealId + "'  added to database");
        em.close();
    }

    @Override
    public Set<String> getFavouriteMeals(String username) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        List<String> ids = em.createQuery("SELECT m.mealId FROM FavouriteMealPojo m WHERE m.username = :username")
                .setParameter("username", username).getResultList();

        em.getTransaction().commit();
        em.close();

        return ids.stream().map(String::valueOf).collect(Collectors.toSet());
    }

    @Override
    public boolean isFavouriteMeal(String username, String mealId) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        Long count = (Long) em.createQuery("SELECT count(m) FROM FavouriteMealPojo m WHERE m.username = :username AND m.mealId = :mealId")
                .setParameter("username", username)
                .setParameter("mealId", mealId)
                .getSingleResult();

        em.close();
        return count > 0;
    }
}
