package pjv.dao;

import org.junit.Assert;
import org.junit.Before;
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
import pjv.model.Exam;
import pjv.model.Subject;
import pjv.model.Teacher;
import pjv.model.User;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan(basePackageClasses = Main.class)
@ActiveProfiles("test")
public class ExamDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExamDao examDao;

    Teacher teacher;

    List<Exam> examsList;

    List<Exam> examsListForTeacher;

    @Before
    public void init() {
        User user = Generator.getUser();
        entityManager.persist(user);
        teacher = Generator.getTeacher();
        teacher.setUser(user);
        entityManager.persist(teacher);
        examsList = new ArrayList<>();
        Exam exam = Generator.getExam();
        Subject subject = Generator.getSubject();
        for (int i = 0; i < 3; i++) {
            subject = Generator.getSubject();
            entityManager.persist(subject);
            exam = Generator.getExam();
            exam.setSubject(subject);
            examsList.add(exam);
        }
        examsList.get(0).setTeacher(teacher);
        examsList.get(1).setTeacher(teacher);
        Teacher teacher2 = Generator.getTeacher();
        User user2 = Generator.getUser();
        entityManager.persist(user2);
        teacher2.setUser(user2);
        entityManager.persist(teacher2);
        examsList.get(2).setTeacher(teacher2);

        for (Exam e : examsList) {
            entityManager.persist(e);
        }
    }

    @Test
    public void testFindExamsByTeacherId() {
        //arrange
        int expected = 2;
        //act
        List<Exam> actualList = examDao.findExamsByTeacherId(teacher.getId());
        //assert
        Assert.assertEquals(expected, actualList.size());

    }

    @Test
    public void testFindAllExams() {
        //act
        List<Exam> actualList = examDao.findAll();
        //assert
        Assert.assertEquals(examsList.size(), actualList.size());
    }

    @Test
    public void testRemoveExam() {
        //arrange
        Exam examToRemove = examsList.get(0);
        //act
        examDao.remove(examToRemove);
        //assert
        Assert.assertNull(examDao.find(examToRemove.getId()));
    }

    @Test
    public void testUpdateExam() {
        //arrange
        Exam examToUpdate = examsList.get(0);
        String expected = "16:00";
        examToUpdate.setTime(expected);
        //act
        examDao.update(examToUpdate);
        //assert
        Assert.assertEquals(expected, examToUpdate.getTime());
    }

    @Test
    public void testFindExamById() {
        //arrange
        Exam expectedExam = examsList.get(1);
        //act
        Exam actual = examDao.find(expectedExam.getId());
        //assert
        Assert.assertEquals(expectedExam, actual);
    }



}
