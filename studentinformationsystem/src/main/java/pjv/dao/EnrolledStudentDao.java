package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.EnrolledStudent;

import javax.persistence.Query;
import java.util.List;

@Repository
public class EnrolledStudentDao extends BaseDao<EnrolledStudent> {
    public EnrolledStudentDao() {
        super(EnrolledStudent.class);
    }

//    public List<Integer> findStudentIDForSubject(Integer id) {
//        Query query = em.createNativeQuery("SELECT student_id FROM enrolledStudents WHERE subject_id = ?1");
//        query.setParameter(1, id);
//        return query.getResultList();
//    }

    public List<EnrolledStudent> findStudentsForSubject(Integer id) {
        Query query = em.createQuery("SELECT s FROM EnrolledStudent as s WHERE s.subject.id = ?1", EnrolledStudent.class);
        query.setParameter(1, id);
        return query.getResultList();
    }

    public List<EnrolledStudent> findByStudentId(Integer id) {
        Query query = em.createQuery("Select s from EnrolledStudent as s where s.student.id = :id", EnrolledStudent.class).setParameter("id", id);
        return query.getResultList();
    }
}
