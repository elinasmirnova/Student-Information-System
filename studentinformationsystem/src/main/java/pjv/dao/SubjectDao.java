package pjv.dao;

import pjv.model.Subject;

public class SubjectDao extends BaseDao<Subject> {
    protected SubjectDao(Class<Subject> type) {
        super(type);
    }
}
