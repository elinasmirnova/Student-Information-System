package pjv.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import pjv.Main;
import pjv.model.Assignment;
import pjv.model.Exam;
import pjv.model.Subject;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Main.class)
public class DaoTest {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected EnrolledStudentDao enrolledStudentDao;

    @Autowired
    protected ExamDao examDao;

    @Autowired
    protected StudentDao studentDao;

    @Autowired
    protected SubjectDao subjectDao;

    @Autowired
    protected TeacherDao teacherDao;

    @Autowired
    protected AssignmentDao assignmentDao;

    @Test
    public void testRepositoriesInApplicationContext(){
        //assert
        Assert.assertNotNull(userDao);
        Assert.assertNotNull(enrolledStudentDao);
        Assert.assertNotNull(examDao);
        Assert.assertNotNull(studentDao);
        Assert.assertNotNull(subjectDao);
        Assert.assertNotNull(teacherDao);
        Assert.assertNotNull(assignmentDao);

    }
}
