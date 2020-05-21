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
import pjv.model.Subject;
import pjv.model.Teacher;
import pjv.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class SubjectServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SubjectService subjectService;

    List<Subject> subjectsList = new ArrayList<>();

    Teacher teacher;

    List<Teacher> teachersList = new ArrayList<>();

    @Before
    public void init() {
        teacher = Generator.getTeacher();
        Subject subject;
        User user = Generator.getUser();
        em.persist(user);
        teacher.setUser(user);
        em.persist(teacher);
        teachersList.add(teacher);

        for (int i = 0; i < 2; i++) {
            subject = Generator.getSubject();
            subject.setTeachers(teachersList);
            em.persist(subject);
            subjectsList.add(subject);
        }

        teacher.setSubjects(subjectsList);
        em.merge(teacher);

    }

    @Test
    public void testFindSubjectsIDForTeacher() {
        //arrange
        List<Integer> expected = new ArrayList<>();
        expected.add(subjectsList.get(0).getId());
        expected.add(subjectsList.get(1).getId());
        //act
        List<Integer> actualSubjectIDs = subjectService.findSubjectsIDForTeacher(teacher.getId());
        //assert
        Assert.assertTrue(actualSubjectIDs.contains(expected.get(0)));
        Assert.assertTrue(actualSubjectIDs.contains(expected.get(1)));
        Assert.assertEquals(expected.size(), actualSubjectIDs.size());
    }

    @Test
    public void findSubjectByCode() {
        //arrange
        Subject expected = subjectsList.get(0);

        //act
        Subject actual = subjectService.findSubjectByCode(subjectsList.get(0).getCode());

        //assert
        Assert.assertEquals(expected, actual);

    }
}
