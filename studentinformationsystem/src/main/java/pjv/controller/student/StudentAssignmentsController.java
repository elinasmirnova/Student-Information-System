package pjv.controller.student;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.LoginController;
import pjv.model.Assignment;
import pjv.model.Student;
import pjv.model.Subject;
import pjv.service.AssignmentService;
import pjv.service.EnrolledStudentService;
import pjv.service.StudentService;
import pjv.service.SubjectService;
import pjv.view.FxmlView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class StudentAssignmentsController implements Initializable {

    @FXML
    private TableView<Assignment> assignmentsTable;

    @FXML
    private TableColumn<Assignment, Integer> idColumn;

    @FXML
    private TableColumn<Assignment, String> subjectColumn;

    @FXML
    private TableColumn<Assignment, String> titleColumn;

    @FXML
    private TableColumn<Assignment, LocalDate> deadlineColumn;

    @FXML
    private Label assignmentTitle;

    @FXML
    private JFXTextField deadlineTxtField;

    @FXML
    private TextFlow descriptionTxtFlow;

    Student student;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private AssignmentService assignmentService;

    @Lazy
    @Autowired
    private StageManager stageManager;

    private ObservableList<Assignment> assignmentsList = FXCollections.observableArrayList();


    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void toExams(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_EXAMS);
    }

    @FXML
    void toHome(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_MAIN);
    }

    @FXML
    void toPersonalInfo(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_PERSONAL_INFO);
    }

    @FXML
    void toStudyResults(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_STUDY_RESULTS);
    }

    @FXML
    void toSubjects(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_SUBJECTS);
    }


    private void setColumnProperties(){
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject().getCode()));
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        deadlineColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
    }

    private void loadAssignments() {
        assignmentsList.clear();
        List<Integer> subjectsIds =  enrolledStudentService.findSubjectsIDByStudentId(student.getId());
        List<Subject> subjects = new ArrayList<>();
        for (Integer id: subjectsIds) {
            subjects.add(subjectService.find(id));
        }
        for (Subject s : subjects) {
            assignmentsList.addAll(assignmentService.findAllBySubjectCode(s.getCode()));
        }
        //assignmentsList.addAll(studentService.findAll());
        assignmentsTable.setItems(assignmentsList);
    }

    private void fillTextFields() {
        descriptionTxtFlow.getChildren().clear();
        Assignment assignment = assignmentsTable.getSelectionModel().getSelectedItem();
        assignmentTitle.setText(assignment.getName());
        deadlineTxtField.setText(assignment.getDeadline().toString());
        Text text = new Text(assignment.getDescription());
        descriptionTxtFlow.getChildren().add(text);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        student = studentService.findByUsername(LoginController.authorizationLogin);
        setColumnProperties();
        loadAssignments();
        assignmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillTextFields();
//                assignmentsTable.getSelectionModel().clearSelection();
            }
        });

    }
}
