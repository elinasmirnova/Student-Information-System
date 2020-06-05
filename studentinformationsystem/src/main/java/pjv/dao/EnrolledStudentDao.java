package pjv.dao;

import org.springframework.stereotype.Repository;
import pjv.model.EnrolledStudent;

import javax.persistence.Query;
import java.util.List;

/**
 * Repository for the entity, which relates to the ManyToMany relation between Subject and Student
 */
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

    /**
     * Selects and returns students which were enrolled to the certain subject
     * @param id subject id
     * @return
     */
    public List<EnrolledStudent> findStudentsForSubject(Integer id) {
        Query query = em.createQuery("SELECT s FROM EnrolledStudent as s WHERE s.subject.id = ?1", EnrolledStudent.class);
        query.setParameter(1, id);
        return query.getResultList();
    }

    public List<EnrolledStudent> findByStudentId(Integer id) {
        Query query = em.createQuery("Select s from EnrolledStudent as s where s.student.id = :id", EnrolledStudent.class).setParameter("id", id);
        return query.getResultList();
    }

    /**
     * Selects and returns subjects id filtered by students
     * @param id student id
     * @return
     */
    public List<Integer> findSubjectsIDByStudentId(Integer id) {
        Query q = em.createNativeQuery("SELECT subject_id FROM enrolledStudents WHERE student_id = ?1").setParameter(1, id);
        return q.getResultList();
    }

    /**
     * Selects and returns subjects which has been already completed by the certain student
     * @param id student id
     * @return
     */
    public List<Integer> findCompletedSubjectsIDByStudentId(Integer id) {
        Query q = em.createNativeQuery("SELECT subject_id FROM enrolledStudents " +
                "WHERE student_id = ?1 and grade is not null and completed = true")
                .setParameter(1, id);
        return q.getResultList();
    }

    /**
     * Selects and returns grade filtered by the student and the subject
     * @param studentId
     * @param subjectId
     * @return
     */
    public Object findGradeByStudentIdAndSubjectId(Integer studentId, Integer subjectId) {
        Query q = em.createNativeQuery("SELECT grade FROM enrolledStudents" +
                " WHERE student_id = ?1 AND subject_id = ?2")
                .setParameter(1, studentId).setParameter(2, subjectId);
        return q.getSingleResult();
    }

    /**
     * Selects and returns subjects ids filtered by the certain student
     * @param id student id
     * @return
     */
    public List<Integer> findEnrolledSubjectsByStudentId(Integer id) {
        Query q = em.createNativeQuery("SELECT subject_id FROM enrolledStudents " +
                "WHERE student_id = ?1 and grade is null and completed = false")
                .setParameter(1, id);
        return q.getResultList();
    }

    public void deleteEnrolledStudent(Integer studentId, Integer subjectId) {
        em.createNativeQuery("DELETE FROM enrolledstudents WHERE student_id = ?1 AND subject_id = ?2")
                .setParameter(1, studentId ).setParameter(2, subjectId).executeUpdate();;
    }

}
