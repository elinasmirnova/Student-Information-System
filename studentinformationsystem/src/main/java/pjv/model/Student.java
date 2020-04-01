package pjv.model;

import javax.persistence.*;
import java.util.List;

public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String year;
    private String specialization;
    private String address;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subject> subjects;





}
