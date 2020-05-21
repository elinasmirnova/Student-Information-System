package pjv.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import pjv.Generator;
import pjv.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private UserService userService;

    User user1;

    User user2;

    @Before
    public void init() {
        user1 = Generator.getUser();
        user1.setRole("admin");
        em.persist(user1);

        user2 = Generator.getUser();
        user2.setRole("teacher");
        em.persist(user2);

    }

    @Test
    public void testCheckRoleByUsername_Admin() {
        //arrange
        String expected = user1.getRole();
        //act
        String actual = userService.checkRole(user1.getUsername());
        //assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCheckRoleByUsername_Teacher() {
        //arrange
        String expected = user2.getRole();
        //act
        String actual = userService.checkRole(user2.getUsername());
        //assert
        Assert.assertEquals(expected, actual);
    }


}
