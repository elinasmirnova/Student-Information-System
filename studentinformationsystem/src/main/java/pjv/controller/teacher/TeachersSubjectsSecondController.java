package pjv.controller.teacher;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import pjv.Main;
import pjv.model.EnrolledStudent;
import pjv.model.Student;
import pjv.model.Subject;
import pjv.service.EnrolledStudentService;
import pjv.service.StudentService;
import pjv.service.SubjectService;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the modal scene on the new stage
 */
@Controller
public class TeachersSubjectsSecondController  implements Initializable {

    @FXML
    private TableView<Student> table1;

    @FXML
    private TableColumn<Student, Integer> table1ColID;

    @FXML
    private TableColumn<Student, String> table1ColFirstName;

    @FXML
    private TableColumn<Student, String> table1ColLastName;

    @FXML
    private Button buttonTo;

    @FXML
    private Button buttonBack;

    @FXML
    private TableView<EnrolledStudent> table2;

    @FXML
    private TableColumn<EnrolledStudent, Integer> table2ColID;

    @FXML
    private TableColumn<EnrolledStudent, String> table2ColFirstName;

    @FXML
    private TableColumn<EnrolledStudent, String> table2ColLastName;

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private StudentService studentService;

    private ObservableList<Student> list1 = FXCollections.observableArrayList();
    private List<Student> allStudents = new ArrayList<>();
    private ObservableList<EnrolledStudent> list2 = FXCollections.observableArrayList(TeacherSubjectsController.studentsList);

    private void fillTable1() {
        list1.clear();
        allStudents.clear();
        studentService = Main.context.getBean(StudentService.class);
        allStudents.addAll(studentService.findAll());
        for (EnrolledStudent student: list2) {
            allStudents.removeIf(s -> s.getId().equals(student.getStudent().getId()));
        }
        list1.addAll(allStudents);
        table1.setItems(list1);
    }

    private void fillTable2() {
        table2.setItems(list2);
    }

    private void updateTables() {
        table1.getItems().clear();
        table2.getItems().clear();
        table1.setItems(list1);
        table2.setItems(list2);
    }

    private void setColumnPropertiesForTable1(){
        table1ColID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        table1ColFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        table1ColLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
    }

    private void setColumnPropertiesForTable2(){
        table2ColID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getStudent().getId()));
        table2ColFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getFirstName()));
        table2ColLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getLastName()));
    }

    /**
     * Initializing scene, sets column properties for 2 tables (the 1. table contains the list of the students,
     * which are not enrolled to the course, the 2. table contains students list, who was enrolled to the subject)
     * loads these students lists and sets them to the tables
     * sets actions on the button, which allows to enroll/unenroll student
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumnPropertiesForTable1();
        setColumnPropertiesForTable2();
        fillTable1();
        fillTable2();
        buttonTo.setOnAction((ActionEvent event) -> {
            Student potential = table1.getSelectionModel()
                    .getSelectedItem();
            if (potential != null) {
                table1.getSelectionModel().clearSelection();
                list1.remove(potential);
                EnrolledStudent newStudent = new EnrolledStudent();
                newStudent.setStudent(potential);
                newStudent.setSubject(list2.get(0).getSubject());
                enrolledStudentService = Main.context.getBean(EnrolledStudentService.class);
                enrolledStudentService.persist(newStudent);
                updateTables();
            }
        });

        buttonBack.setOnAction((ActionEvent event) -> {
            EnrolledStudent potential = table2.getSelectionModel()
                    .getSelectedItem();
            if (potential != null) {
                table2.getSelectionModel().clearSelection();
                list2.remove(potential);
                Student fuckingStudent = studentService.find(potential.getStudent().getId());
                list1.add(studentService.find(fuckingStudent.getId()));
//                fuckingStudent.removeEnrolledStudent(potential);
//                studentService.update(fuckingStudent);
//                list2.get(0).getSubject().removeEnrolledStudent(potential);
//                subjectService = Main.context.getBean(SubjectService.class);
//                subjectService.update(list2.get(0).getSubject());
                enrolledStudentService = Main.context.getBean(EnrolledStudentService.class);
                enrolledStudentService.remove(potential);
                updateTables();
            }
        });
    }

}
