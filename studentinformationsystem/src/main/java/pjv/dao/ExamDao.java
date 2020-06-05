package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Assignment;
import pjv.model.Exam;
import pjv.model.Student;

import java.util.List;

/**
 * Repository for the Exam entity
 */
@Repository
public class ExamDao extends BaseDao<Exam>  {
    public ExamDao() {
        super(Exam.class);
    }

    /**
     * Selects and returns exams list filtered by the certain teacher
     * @param id teacher id
     * @return
     */
    public List<Exam> findExamsByTeacherId(Integer id) {
        return em.createQuery(
                "SELECT e FROM Exam AS e WHERE e.teacher.id = :id", Exam.class).setParameter("id", id).getResultList();
    }

    /**
     * Selects and return available to enroll exams filtered by the certain subject
     * @param code subject code
     * @return
     */
    public List<Exam> findAvailableToEnrollExamsBySubject(String code) {
        return em.createQuery("SELECT e FROM Exam AS e WHERE e.subject.code = :code AND e.available = true")
                .setParameter("code", code).getResultList();
    }

}
