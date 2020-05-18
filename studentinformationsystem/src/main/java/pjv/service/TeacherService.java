package pjv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.TeacherDao;
import pjv.model.Teacher;

import java.util.List;

@Service
public class TeacherService {

    private TeacherDao teacherDao;

    @Autowired
    public TeacherService(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Transactional
    public void persist(Teacher teacher) {
        teacherDao.persist(teacher);
    }

    @Transactional
    public void update(Teacher teacher) {
        teacherDao.update(teacher);
    }

    @Transactional
    public void remove(Teacher teacher) {
        teacherDao.remove(teacher);
    }

    @Transactional
    public Teacher find(int id) {
        return teacherDao.find(id);
    }

    @Transactional
    public List<Teacher> findAll() {
        return teacherDao.findAll();
    }

    @Transactional
    public Teacher findByUsername(String username) {
        return teacherDao.findByUsername(username);
    }

}
