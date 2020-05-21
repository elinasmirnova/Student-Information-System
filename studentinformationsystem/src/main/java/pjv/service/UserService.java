package pjv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pjv.dao.UserDao;
import pjv.model.User;

import java.util.List;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Transactional
    public void persist(User user) {
        userDao.persist(user);
    }

    @Transactional
    public void update(User user) {
        userDao.update(user);
    }

    @Transactional
    public void remove(User user) {
        userDao.remove(user);
    }

    @Transactional
    public User find(int id) {
        return userDao.find(id);
    }

    @Transactional
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Transactional
    public boolean authenticate(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            return user.getPassword().equals(password);
        }

        return false;

    }

    @Transactional
    public String checkRole(String username) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            return user.getRole();
        }
        return null;
    }

    @Transactional
    public boolean ifExists(String username) {
        return userDao.findByUsername(username) != null;
    }

}
