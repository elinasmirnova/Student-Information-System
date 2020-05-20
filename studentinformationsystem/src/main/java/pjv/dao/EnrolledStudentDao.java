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

    public List<Integer> findSubjectsIDByStudentId(Integer id) {
        Query q = em.createNativeQuery("SELECT subject_id FROM enrolledStudents WHERE student_id = ?1").setParameter(1, id);
        return q.getResultList();
    }

    public List<Integer> findCompletedSubjectsIDByStudentId(Integer id) {
        Query q = em.createNativeQuery("SELECT subject_id FROM enrolledStudents " +
                "WHERE student_id = ?1 and grade is not null and completed = true")
                .setParameter(1, id);
        return q.getResultList();
    }

    public Object findGradeByStudentIdAndSubjectId(Integer studentId, Integer subjectId) {
        Query q = em.createNativeQuery("SELECT grade FROM enrolledStudents" +
                " WHERE student_id = ?1 AND subject_id = ?2")
                .setParameter(1, studentId).setParameter(2, subjectId);
        return q.getSingleResult();
    }

    public List<Integer> findEnrolledSubjectsByStudentId(Integer id) {
        Query q = em.createNativeQuery("SELECT subject_id FROM enrolledStudents " +
                "WHERE student_id = ?1 and grade is null and completed = false")
                .setParameter(1, id);
        return q.getResultList();
    }

}
