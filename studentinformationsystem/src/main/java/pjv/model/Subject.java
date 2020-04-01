package pjv.model;

import javax.persistence.*;
import java.util.List;

public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer code;
    private String name;
    private Integer credits;
    private String semester;
    private String synopsis;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teacher> teachers;


}
