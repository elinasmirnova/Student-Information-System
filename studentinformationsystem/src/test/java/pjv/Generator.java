package pjv;

import pjv.model.Exam;
import pjv.model.Subject;
import pjv.model.Teacher;
import pjv.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.Random;

public class Generator {

    @PersistenceContext
    private static EntityManager em;

    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static User getUser() {
        final User user = new User();
        //user.setId(1);
        user.setUsername("testtesttest" + randomInt());
        user.setRole("student");
        user.setPassword("1010");

        return user;
    }

    public static Teacher getTeacher() {
        final Teacher teacher = new Teacher();
        teacher.setDegree("test");
        teacher.setDepartment("testovaci department");
        teacher.setFirstName("Petr");
        teacher.setLastName("Petrov");
        teacher.setUser(getUser());

        return teacher;
    }

    public static Subject getSubject() {
        final Subject subject = new Subject();
        subject.setCode("CODETEST" + randomInt());
        subject.setSemester("Summer");
        subject.setRole("PV");
        subject.setCredits(5);
        subject.setName("Predmet nejaky");

        return subject;
    }


    public static Exam getExam() {
        final Exam exam = new Exam();
        exam.setAvailable(true);
        exam.setCapacity(50);
//        exam.setTeacher(getTeacher());
        exam.setDate(LocalDate.of(2020, 6, 5));
        exam.setTime("12:00");
        exam.setClassroom("Classroom1");

        return exam;
    }

}
