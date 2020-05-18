package pjv.controller.teacher;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.Main;
import pjv.config.StageManager;
import pjv.controller.LoginController;
import pjv.model.EnrolledStudent;
import pjv.model.Subject;
import pjv.model.Teacher;
import pjv.service.EnrolledStudentService;
import pjv.service.SubjectService;
import pjv.service.TeacherService;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class TeacherSubjectsController implements Initializable {

    @FXML
    private Label mainLabel;

    @FXML
    private Label subjectId;

    @FXML
    private TextField code;

    @FXML
    private TableView<EnrolledStudent> studentsTable;

    @FXML
    private TableColumn<EnrolledStudent, Integer> studentsColId;

    @FXML
    private TableColumn<EnrolledStudent, String> studentsColFirstName;

    @FXML
    private TableColumn<EnrolledStudent, String> studentsColLastName;

    @FXML
    private TableColumn<EnrolledStudent, Boolean> studentsColCompleted;

    @FXML
    private TableColumn<EnrolledStudent, String> studentsColGrade;

    @FXML
    private MenuItem viewStudent;

    @FXML
    private Button reset;

    @FXML
    private Button editStudentsList;

    @FXML
    private Button btnLogout;

    @FXML
    private TableView<Subject> subjectsTable;

    @FXML
    private TableColumn<Subject, String> colUserId;

    @FXML
    private TableColumn<Subject, String> colCode;

    @FXML
    private TableColumn<Subject, String> colName;

    @FXML
    private TableColumn<Subject, String> colRole;

    @FXML
    private TableColumn<Subject, Integer> colCredits;

    @FXML
    private TableColumn<Subject, String> colSemester;

    @FXML
    private MenuItem viewSubject;

    @FXML
    private MenuItem deleteUsers1;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    Teacher teacher;

    private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();
    public static ObservableList<EnrolledStudent> studentsList = FXCollections.observableArrayList();

    @FXML
    void deleteSubject(ActionEvent event) {

    }

    @FXML
    void editStudentsList(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/teacher/teacher_subjects2.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage newWindow = new Stage();
        newWindow.setTitle("Edit students list");
        newWindow.setScene(new Scene(root1));
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.show();
    }

    @FXML
    void viewMoreAboutStudent(ActionEvent event) {

    }

    @FXML
    void viewMoreAboutSubject(ActionEvent event) {

    }

    private void loadSubjectsDetails() {
        teacher = teacherService.findByUsername(LoginController.authorizationLogin);
        subjectsList.clear();
        List<Subject> subjects = new ArrayList<>();
        for (Integer key : subjectService.findSubjectsIDForTeacher(teacher.getId())) {
            subjects.add(subjectService.find(key));
        }
        subjectsList.addAll(subjects);
        //subjectsList.addAll(subjectService.(teacher.getId()));
        subjectsTable.setItems(subjectsList);
    }

    private void setColumnSubjectsProperties(){
        colUserId.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getId()));
        colCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colCredits.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCredits()));
        colRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        colSemester.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemester()));

    }

    private void setColumnStudentsProperties() {
        studentsColId.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getStudent().getId()));
        studentsColFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getFirstName()));
        studentsColLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getLastName()));
       // studentsColCompleted.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isCompleted() ? "yes" : "no"));
        studentsColGrade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade()));
    }

    public void updateTable() {
        loadSubjectsDetails();
    }

    public void loadStudentsListForSubject(Subject subject) {
        enrolledStudentService = Main.context.getBean(EnrolledStudentService.class);
        subjectId.setText(subject.getId().toString());
        code.setText(subject.getCode());
        studentsList.clear();
//        List<EnrolledStudent> students = enrolledStudentService.findStudentForSubject(subject.getId());


//        List<Integer> keys = enrolledStudentService.findStudentIDForSubject(subject.getId());
//        for (Integer key : keys) {
//            students.add((EnrolledStudent) enrolledStudentService.findByStudentId(key));
//        }

       studentsList.addAll(subject.getEnrolledStudents());


       // studentsList.addAll(students);


        studentsTable.setItems(studentsList);
    }

    public void reset() {
        subjectId.setText(null);
        code.clear();
        studentsTable.getItems().clear();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setColumnSubjectsProperties();
        setColumnStudentsProperties();
        loadSubjectsDetails();
        code.setDisable(true);

        studentsTable.setEditable(true);

        subjectsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadStudentsListForSubject(subjectsTable.getSelectionModel().getSelectedItem());
                //userTable.getSelectionModel().clearSelection();
            }
        });

        studentsColCompleted.setCellValueFactory(param -> {
            EnrolledStudent student = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(student.isCompleted());

            booleanProperty.addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    student.setCompleted(newValue);
                    enrolledStudentService.update(student);
                }
            });
            return booleanProperty;
        });

        studentsColCompleted.setCellFactory(param -> {
            CheckBoxTableCell<EnrolledStudent, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        studentsColGrade.setCellFactory(TextFieldTableCell.forTableColumn());

        studentsColGrade.setOnEditCommit((TableColumn.CellEditEvent<EnrolledStudent, String> event) -> {
            TablePosition<EnrolledStudent, String> pos = event.getTablePosition();

            String grade = event.getNewValue();

            int row = pos.getRow();
            EnrolledStudent student = event.getTableView().getItems().get(row);
            student.setGrade(grade);
            enrolledStudentService.update(student);
        });

        reset.setOnAction(event -> reset());



    }
}
