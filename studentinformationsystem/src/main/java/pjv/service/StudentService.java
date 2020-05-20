package pjv.service;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.StudentDao;
import pjv.model.Student;
import pjv.model.Teacher;
import pjv.model.User;

import java.util.List;

@Service
//@Configurable(preConstruction = true, autowire = Autowire.BY_NAME)
public class StudentService {

    private StudentDao studentDao;

    @Autowired
    public StudentService(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Transactional
    public void persist(Student student) {
        studentDao.persist(student);
    }

    @Transactional
    public void update(Student student) {
        studentDao.update(student);
    }

    @Transactional
    public void remove(Student student) {
        studentDao.remove(student);
    }

    @Transactional
    public Student find(int id) {
        return studentDao.find(id);
    }

    @Transactional
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Transactional
    public Student findByUsername(String username) {
        return studentDao.findByUsername(username);
    }

}
