package pjv.controller.student;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.LoginController;
import pjv.model.Student;
import pjv.model.StudyResultPOJO;
import pjv.model.Subject;
import pjv.service.EnrolledStudentService;
import pjv.service.StudentService;
import pjv.service.SubjectService;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class StudentSubjectsController implements Initializable {

    @FXML
    private TableView<Subject> table;

    @FXML
    private TableColumn<Subject, String> subjectColumn;

    @FXML
    private TableColumn<Subject, String> nameColumn;

    @FXML
    private TableColumn<Subject, String> semesterColumn;

    @FXML
    private TableColumn<Subject, Integer> creditsColumn;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SubjectService subjectService;

    Student student;

    private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void toAssignments(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_ASSIGNMENTS);
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

    private void loadStudyResults() {
        List<Integer> subjectIDs = enrolledStudentService.findEnrolledSubjectsByStudentId(student.getId());
        Subject subject;
        for (Integer id : subjectIDs) {
            subject = subjectService.find(id);
            subjectsList.add(subject);

        }
        table.setItems(subjectsList);
    }

    private void setColumnProperties() {
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
        semesterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemester()));
        creditsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCredits()));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        student = studentService.findByUsername(LoginController.authorizationLogin);
        setColumnProperties();
        loadStudyResults();
    }
}
