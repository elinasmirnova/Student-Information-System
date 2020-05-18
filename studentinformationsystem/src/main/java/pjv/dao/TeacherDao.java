package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Teacher;

import javax.persistence.NoResultException;

@Repository
public class TeacherDao extends BaseDao<Teacher>  {
    public TeacherDao() {
        super(Teacher.class);
    }

    public Teacher findByUsername(String username) {
        try {
            return em.createNamedQuery("Teacher.findByUsername", Teacher.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
