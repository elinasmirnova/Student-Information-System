package pjv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.AssignmentDao;
import pjv.model.Assignment;
import pjv.model.Exam;
import pjv.model.Subject;

import java.util.List;

@Service
public class AssignmentService {

    private AssignmentDao dao;

    @Autowired
    public AssignmentService(AssignmentDao dao) {
        this.dao = dao;
    }

    @Transactional
    public void persist(Assignment assignment) {
        dao.persist(assignment);
    }

    @Transactional
    public void update(Assignment assignment) {
        dao.update(assignment);
    }

    @Transactional
    public void remove(Assignment assignment) {
        dao.remove(assignment);
    }

    @Transactional
    public Assignment find(int id) {
        return dao.find(id);
    }

    @Transactional
    public List<Assignment> findAll() {
        return dao.findAll();
    }

    @Transactional
    public List<Assignment> findAssignmentsByTeacherId(Integer id) {
        return dao.findAssignmentsByTeacherId(id);
    }

}
