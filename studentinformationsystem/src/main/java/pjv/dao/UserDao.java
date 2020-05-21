package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.User;

import javax.persistence.NoResultException;

/**
 * Repository for the User entity
 */
@Repository
public class UserDao extends BaseDao<User> {
    public UserDao() {
        super(User.class);
    }

    /**
     * Finds user by the username
     * @param username
     * @return User entity
     */
    public User findByUsername(String username) {
        try {
            return em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
