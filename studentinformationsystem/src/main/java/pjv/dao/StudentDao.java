package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Student;
import pjv.model.User;

import javax.persistence.*;
import java.util.List;

@Repository
public class StudentDao extends BaseDao<Student>  {
    public StudentDao() {
        super(Student.class);
    }

    public Student findByUsername(String username) {
        try {
            return em.createNamedQuery("Student.findByUsername", Student.class).setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Student> customFindAll() {
        List<Student> students = em.createQuery(
                "SELECT s FROM Student", Student.class).getResultList();
        return students;
    }

//    public List<Student> customFindAll() {
//        List<Student> students = em.createNamedQuery(
//                "customFindAll", Student.class).getResultList();
//        return students;
//    }
}
