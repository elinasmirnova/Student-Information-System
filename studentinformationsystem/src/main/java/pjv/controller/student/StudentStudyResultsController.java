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
import pjv.model.*;
import pjv.service.EnrolledStudentService;
import pjv.service.StudentService;
import pjv.service.SubjectService;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the scene with the student's study results
 */
@Controller
public class StudentStudyResultsController implements Initializable {

    @FXML
    private TableView<StudyResultPOJO> table;

    @FXML
    private TableColumn<StudyResultPOJO, String> subjectColumn;

    @FXML
    private TableColumn<StudyResultPOJO, String> nameColumn;

    @FXML
    private TableColumn<StudyResultPOJO, String> semesterColumn;

    @FXML
    private TableColumn<StudyResultPOJO, Integer> creditsColumn;

    @FXML
    private TableColumn<StudyResultPOJO, String> gradeColumn;

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

    Logger LOGGER = Logger.getLogger(StudentStudyResultsController.class.getName());

    private ObservableList<StudyResultPOJO> subjectsList = FXCollections.observableArrayList();

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Student was logged out");
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
    void toSubjects(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_SUBJECTS);
    }

    private void loadStudyResults() {
        table.getItems().clear();
        subjectsList.clear();
        List<Integer> subjectIDs = enrolledStudentService.findCompletedSubjectsIDByStudentId(student.getId());
        StudyResultPOJO row = new StudyResultPOJO();
        Subject subject;
        for (Integer id : subjectIDs) {
            subject = subjectService.find(id);
            row.setSubjectCode(subject.getCode());
            row.setSubjectName(subject.getName());
            row.setCredits(subject.getCredits());
            row.setSemester(subject.getSemester());
            row.setGrade(String.valueOf(enrolledStudentService.findGradeByStudentIdAndSubjectId(student.getId(), subject.getId())));
            subjectsList.add(row);

        }
        table.setItems(subjectsList);
    }

    private void setColumnProperties() {
        subjectColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectCode()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubjectName()));
        semesterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemester()));
        creditsColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCredits()));
        gradeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGrade()));
    }

    /**
     * Initializing scene, sets column properties in the table, loads study results of the student (only completed subjects)
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        student = studentService.findByUsername(LoginController.authorizationLogin);
        setColumnProperties();
        loadStudyResults();
        LOGGER.info("Canvas was initialized");
    }
}
