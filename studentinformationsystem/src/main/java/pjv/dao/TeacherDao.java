package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Teacher;

import javax.persistence.NoResultException;

/**
 * Repository for the Teacher entity
 */
@Repository
public class TeacherDao extends BaseDao<Teacher>  {
    public TeacherDao() {
        super(Teacher.class);
    }

    /**
     * Finds teacher by the username
     * @param username
     * @return Teacher entity
     */
    public Teacher findByUsername(String username) {
        try {
            return em.createNamedQuery("Teacher.findByUsername", Teacher.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
