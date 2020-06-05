package pjv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.EnrolledStudentDao;
import pjv.model.EnrolledStudent;
import pjv.model.Subject;
import pjv.model.Teacher;

import java.util.List;

/**
 * Service for Enrolled Student entity (@ManyToMany relation)
 */
@Service
public class EnrolledStudentService {

    private EnrolledStudentDao dao;

    @Autowired
    public EnrolledStudentService(EnrolledStudentDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(EnrolledStudent student) {
        dao.persist(student);
    }

    @Transactional
    public void update(EnrolledStudent student) {
        dao.update(student);
    }

    @Transactional
    public void remove(EnrolledStudent student) {
        dao.remove(student);
    }

    @Transactional
    public EnrolledStudent find(int id) {
        return dao.find(id);
    }

    @Transactional
    public List<EnrolledStudent> findAll() {
        return dao.findAll();
    }

    /**
     * @see EnrolledStudentDao
     * @param id
     * @return
     */
    @Transactional
    public List<EnrolledStudent> findStudentForSubject(Integer id) {
        return dao.findStudentsForSubject(id);
    }

    /**
     * @see EnrolledStudentDao
     * @param id
     * @return
     */
    @Transactional
    public List<EnrolledStudent> findByStudentId(Integer id) {
        return dao.findByStudentId(id);
    }

    /**
     * @see EnrolledStudentDao
     * @param id
     * @return
     */
    @Transactional
    public List<Integer> findSubjectsIDByStudentId(Integer id) {
        return dao.findSubjectsIDByStudentId(id);
    }

    /**
     * @see EnrolledStudentDao
     * @param id
     * @return
     */
    @Transactional
    public List<Integer> findCompletedSubjectsIDByStudentId(Integer id) {
        return dao.findCompletedSubjectsIDByStudentId(id);
    }

    /**
     * @see EnrolledStudentDao
     * @param studentId
     * @param subjectId
     * @return
     */
    @Transactional
    public Object findGradeByStudentIdAndSubjectId(Integer studentId, Integer subjectId) {
        return dao.findGradeByStudentIdAndSubjectId(studentId, subjectId);
    }

    /**
     * @see EnrolledStudentDao
     * @param id
     * @return
     */
    @Transactional
    public List<Integer> findEnrolledSubjectsByStudentId(Integer id) {
        return dao.findEnrolledSubjectsByStudentId(id);
    }

    @Transactional
    public void deleteEnrolledStudent(Integer studentId, Integer subjectId) {
        dao.deleteEnrolledStudent(studentId, subjectId);
    }
}
