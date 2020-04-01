package pjv.dao;

import pjv.model.Admin;

public class AdminDao extends BaseDao<Admin>  {
    protected AdminDao(Class<Admin> type) {
        super(type);
    }
}
