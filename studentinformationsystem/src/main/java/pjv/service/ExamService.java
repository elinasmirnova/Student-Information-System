package pjv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.ExamDao;
import pjv.model.Exam;
import pjv.model.Student;

import java.util.List;

@Service
public class ExamService {

    private ExamDao examDao;

    @Autowired
    public ExamService(ExamDao examDao) {
        this.examDao = examDao;
    }

    @Transactional
    public void persist(Exam exam) {
        examDao.persist(exam);
    }

    @Transactional
    public void update(Exam exam) {
        examDao.update(exam);
    }

    @Transactional
    public void remove(Exam exam) {
        examDao.remove(exam);
    }

    @Transactional
    public Exam find(int id) {
        return examDao.find(id);
    }

    @Transactional
    public List<Exam> findAll() {
        return examDao.findAll();
    }

    @Transactional
    public List<Exam> findExamsByTeacherId(Integer id) {
        return examDao.findExamsByTeacherId(id);
    }
}
