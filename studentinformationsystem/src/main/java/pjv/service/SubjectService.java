package pjv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.SubjectDao;
import pjv.model.Subject;

import java.util.List;

@Service
public class SubjectService {

    private SubjectDao subjectDao;

    @Autowired
    public SubjectService(SubjectDao subjectDao) {
        this.subjectDao = subjectDao;
    }

    @Transactional
    public void persist(Subject subject) {
        subjectDao.persist(subject);
    }

    @Transactional
    public void update(Subject subject) {
        subjectDao.update(subject);
    }

    @Transactional
    public void remove(Subject subject) {
        subjectDao.remove(subject);
    }

    @Transactional
    public Subject find(int id) {
        return subjectDao.find(id);
    }

    @Transactional
    public List<Subject> findAll() {
        return subjectDao.findAll();
    }

    @Transactional
    public List<Integer> findSubjectsIDForTeacher(Integer id) {
        return subjectDao.findSubjectsIDForTeacher(id);
    }

    @Transactional
    public Subject findSubjectByCode(String code) {
        return subjectDao.findSubjectByCode(code);
    }

    @Transactional
    public boolean ifExists(String code) {
        return subjectDao.findSubjectByCode(code) == null;
    }
}
