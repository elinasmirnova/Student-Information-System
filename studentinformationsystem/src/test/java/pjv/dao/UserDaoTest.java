package pjv.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pjv.Generator;
import pjv.Main;
import pjv.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Main.class)
@ActiveProfiles("test")
public class UserDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDao dao;

    @Test
    public void testFindByUsername() {
        //arrange
       User user =  Generator.getUser();
       entityManager.persist(user);
       //act
       User expected = dao.findByUsername(user.getUsername());
        //assert
       Assert.assertEquals(expected, user);
    }

}
