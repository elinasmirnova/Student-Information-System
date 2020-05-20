package pjv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.EnrolledStudentDao;
import pjv.model.EnrolledStudent;
import pjv.model.Subject;
import pjv.model.Teacher;

import java.util.List;

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

    @Transactional
    public List<EnrolledStudent> findStudentForSubject(Integer id) {
        return dao.findStudentsForSubject(id);
    }
    @Transactional
    public List<EnrolledStudent> findByStudentId(Integer id) {
        return dao.findByStudentId(id);
    }

    @Transactional
    public List<Integer> findSubjectsIDByStudentId(Integer id) {
        return dao.findSubjectsIDByStudentId(id);
    }

    @Transactional
    public List<Integer> findCompletedSubjectsIDByStudentId(Integer id) {
        return dao.findCompletedSubjectsIDByStudentId(id);
    }

    @Transactional
    public Object findGradeByStudentIdAndSubjectId(Integer studentId, Integer subjectId) {
        return dao.findGradeByStudentIdAndSubjectId(studentId, subjectId);
    }

    @Transactional
    public List<Integer> findEnrolledSubjectsByStudentId(Integer id) {
        return dao.findEnrolledSubjectsByStudentId(id);
    }


}
