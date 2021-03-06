package pjv.controller.teacher;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import pjv.service.StudentService;
import pjv.service.SubjectService;
import pjv.service.TeacherService;
import pjv.view.FxmlView;

import javax.swing.text.html.parser.Entity;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the scene, where teachers can look through their courses and enrolled students,
 * also teacher can manage students list and adjust study results
 */
@Controller
public class TeacherSubjectsController implements Initializable {

    @FXML
    private Label mainLabel;

    @FXML
    private Label subjectId;

    @FXML
    private JFXTextField code;

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
    private JFXButton reset;

    @FXML
    private JFXButton editStudentsList;

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

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @Autowired
    private StudentService studentService;

    Teacher teacher;

    private static Subject subject;

    Logger LOGGER = Logger.getLogger(TeacherSubjectsController.class.getName());

    private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();
    public static ObservableList<EnrolledStudent> studentsList = FXCollections.observableArrayList();


    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Teacher was logged out");
    }

    @FXML
    void toAssignments(ActionEvent event) {
        stageManager.switchScene(FxmlView.TEACHER_ASSIGNMENTS);
    }

    @FXML
    void toExams(ActionEvent event) {
        stageManager.switchScene(FxmlView.TEACHER_EXAMS);
    }

    @FXML
    void toHome(ActionEvent event) {
        stageManager.switchScene(FxmlView.TEACHER_MAIN);
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

    private void updateTable() {
        loadSubjectsDetails();
    }

    private void loadStudentsListForSubject(Subject subject) {
        TeacherSubjectsController.subject = subject;
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

    private void reset() {
        subjectId.setText(null);
        code.clear();
        studentsTable.getItems().clear();
    }

    /**
     * Initializing scene, sets column properties to subjects table and students table
     * @param location
     * @param resources
     */
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
                    if (newValue) {
                        student.getStudent().setObtainedCredits(student.getStudent().getObtainedCredits()+student.getSubject().getCredits());
                        studentService.update(student.getStudent());
                    } else {
                        student.getStudent().setObtainedCredits(student.getStudent().getObtainedCredits()-student.getSubject().getCredits());
                        studentService.update(student.getStudent());
                    }
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

        LOGGER.info("Canvas was initialized");

    }

    public static Subject getSubject() {
        return subject;
    }

}
