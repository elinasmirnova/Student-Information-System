package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Exam;
import pjv.model.Subject;

import javax.persistence.*;
import java.util.List;

/**
 * Repository for the Subject entity
 */
@Repository
public class SubjectDao extends BaseDao<Subject> {
    public SubjectDao() {
        super(Subject.class);
    }

    /**
     * Selects and returns subjects list filtered by the certain teacher
     * @param id teacher id
     * @return List of Integers
     */
    public List<Integer> findSubjectsIDForTeacher(Integer id) {
//        List<Subject> subjects = em.createQuery("SELECT s FROM Subject AS s WHERE s.teachers = :teacherId ", Subject.class)
//                                .setParameter("teacherId", id).getResultList();
//        return subjects;
        Query query = em.createNativeQuery("SELECT subject_id from subject_teacher as s " +
                "JOIN Subject ON s.subject_id = subject.id WHERE teacher_id = ?1");
        query.setParameter(1, id);
        return query.getResultList();
    }

    /**
     * Selects and returns subject entity by the subject code
     * @param code subject code
     * @return Subject entity
     */
    public Subject findSubjectByCode(String code) {
        try {
            return em.createQuery(
                    "SELECT s FROM Subject AS s WHERE s.code = :code", Subject.class)
                    .setParameter("code", code)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
