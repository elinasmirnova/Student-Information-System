package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Student;
import pjv.model.User;

import javax.persistence.*;
import java.util.List;

/**
 * Repository for the Student entity
 */
@Repository
public class StudentDao extends BaseDao<Student>  {
    public StudentDao() {
        super(Student.class);
    }

    /**
     * Find the student by the username
     * @param username student username
     * @return Student entity
     */
    public Student findByUsername(String username) {
        try {
            return em.createNamedQuery("Student.findByUsername", Student.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


//    public List<Student> customFindAll() {
//        List<Student> students = em.createNamedQuery(
//                "customFindAll", Student.class).getResultList();
//        return students;
//    }
}
