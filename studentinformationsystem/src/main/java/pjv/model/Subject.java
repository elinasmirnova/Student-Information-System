package pjv.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@SqlResultSetMapping(
//        name = "AllSubjectsForTeacherById",
//        classes = {@ConstructorResult(
//                targetClass = Subject.class,
//                columns = {
//                        @ColumnResult(name = "subject_id", type = Integer.class),
//                        @ColumnResult(name = "code", type = String.class),
//                        @ColumnResult(name = "name", type = String.class),
//                        @ColumnResult(name = "credits", type = Integer.class),
//                        @ColumnResult(name = "role", type = String.class),
//                        @ColumnResult(name = "semester",type = String.class),
//                        @ColumnResult(name = "synopsis", type = String.class)
//                }
//
//        )
//        }
//)
@Entity
@Table(name = "subject")
public class Subject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true)
    @NaturalId
    private String code;

    private String name;
    private Integer credits;
    private String role;
    private String semester;
    private String synopsis;

    @OneToMany(mappedBy = "subject",orphanRemoval = true)
    private List<Exam> exams;

    @OneToMany(mappedBy = "subject",orphanRemoval = true)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
//    @org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public List<EnrolledStudent> enrolledStudents;


    public List<EnrolledStudent> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(List<EnrolledStudent> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void addEnrolledStudent(EnrolledStudent e) {
        Objects.requireNonNull(e);
        if (this.enrolledStudents == null) {
            this.enrolledStudents = new ArrayList<>();
        }
        enrolledStudents.add(e);
    }

    public void removeEnrolledStudent(EnrolledStudent e) {
        Objects.requireNonNull(e);
        enrolledStudents.removeIf(current -> current.getId().equals(e.getId()));
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void addAssignment(Assignment a) {
        Objects.requireNonNull(a);
        if (this.assignments == null) {
            this.assignments = new ArrayList<>();
        }
        assignments.add(a);
    }

    public void removeAssignment(Assignment a) {
        Objects.requireNonNull(a);
        assignments.removeIf(current -> current.getId().equals(a.getId()));
    }



    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "subject_teacher",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    List<Teacher> teachers;

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public void addTeacher(Teacher teacher) {
        Objects.requireNonNull(teacher);
        if (this.teachers == null) {
            this.teachers = new ArrayList<>();
        }
        teachers.add(teacher);
    }

    public void removeTeacher(Teacher teacher) {
        Objects.requireNonNull(teacher);
        teachers.removeIf(current -> current.getId().equals(teacher.getId()));
    }

    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "credits")
    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    @Basic
    @Column(name = "role")
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Basic
    @Column(name = "semester")
    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    @Basic
    @Column(name = "synopsis")
    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}
