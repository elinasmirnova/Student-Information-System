package pjv.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//@NamedQueries({
//        @NamedQuery(name = "Student.findByUsername", query = "SELECT u FROM Student u WHERE u.user.username = :username")
//})
@Entity
@Table(name = "student")
public class Student implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;

    @Column(name = "date_of_birth")
    @Type(type = "pjv.model.LocalDateHibernateUserType")
    private LocalDate dateOfBirth;

    private Integer year;
    private String studyProgram;
    private String address;
    private String phoneNumber;
    private Integer obtainedCredits;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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

    public Integer getId() {
        return id;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Basic
    @Column(name = "year")
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Basic
    @Column(name = "study_program")
    public String getStudyProgram() {
        return studyProgram;
    }

    public void setStudyProgram(String studyProgram) {
        this.studyProgram = studyProgram;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "obtained_credits")
    public Integer getObtainedCredits() {
        return obtainedCredits;
    }

    public void setObtainedCredits(Integer obtainedCredits) {
        this.obtainedCredits = obtainedCredits;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
