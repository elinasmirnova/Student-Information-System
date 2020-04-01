package pjv.dao;

import pjv.model.Student;

public class StudentDao extends BaseDao<Student>  {
    protected StudentDao(Class<Student> type) {
        super(type);
    }
}
