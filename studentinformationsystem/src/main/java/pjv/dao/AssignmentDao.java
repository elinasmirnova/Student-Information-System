package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.Assignment;
import pjv.model.Exam;

import java.util.List;

/**
 * Repository for assignment entity
 */
@Repository
public class AssignmentDao extends BaseDao<Assignment>  {
    public AssignmentDao() {
        super(Assignment.class);
    }

    /**
     * Selects and returns assignments which were added by the certain teacher
     * @param id teacher id
     * @return
     */
    public List<Assignment> findAssignmentsByTeacherId(Integer id) {
        return em.createQuery(
                "SELECT a FROM Assignment AS a WHERE a.teacher.id = :id", Assignment.class).setParameter("id", id).getResultList();
    }

    /**
     * Selects and returns all the assignments filtered by subject code
     * @param code subject code
     * @return
     */
    public List<Assignment> findAllBySubjectCode(String code) {
        return em.createQuery(
                "select a from Assignment a where a.subject.code = :code", Assignment.class)
                .setParameter("code", code).getResultList();

    }
}
