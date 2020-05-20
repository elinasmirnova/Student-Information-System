package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Assignment;
import pjv.model.Exam;
import pjv.model.Student;

import java.util.List;

@Repository
public class ExamDao extends BaseDao<Exam>  {
    public ExamDao() {
        super(Exam.class);
    }

    public List<Exam> findExamsByTeacherId(Integer id) {
        return em.createQuery(
                "SELECT e FROM Exam AS e WHERE e.teacher.id = :id", Exam.class).setParameter("id", id).getResultList();
    }

    public List<Exam> findAvailableToEnrollExamsBySubject(String code) {
        return em.createQuery("SELECT e FROM Exam AS e WHERE e.subject.code = :code AND e.available = true")
                .setParameter("code", code).getResultList();
    }
}
