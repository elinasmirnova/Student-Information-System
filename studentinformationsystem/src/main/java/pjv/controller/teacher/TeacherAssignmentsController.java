package pjv.controller.teacher;

import com.jfoenix.controls.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.LoginController;
import pjv.controller.Validation;
import pjv.controller.student.StudentSubjectsController;
import pjv.model.Assignment;
import pjv.model.Exam;
import pjv.model.Subject;
import pjv.model.Teacher;
import pjv.service.AssignmentService;
import pjv.service.SubjectService;
import pjv.service.TeacherService;
import pjv.view.FxmlView;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the scene, where teacher is allowed to manages assignments for students
 */
@Controller
public class TeacherAssignmentsController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Label assignmentId;

    @FXML
    private JFXComboBox<String> subjectCode;

    @FXML
    private JFXTextField title;

    @FXML
    private JFXDatePicker deadline;

    @FXML
    private JFXTextArea txtAreaDescription;

    @FXML
    private JFXButton reset;

    @FXML
    private JFXButton save;


    @FXML
    private TableView<Assignment> assignmentsTable;

    @FXML
    private TableColumn<Assignment, Integer> colId;

    @FXML
    private TableColumn<Assignment, String> colSubject;

    @FXML
    private TableColumn<Assignment, String> colTitle;

    @FXML
    private TableColumn<Assignment, LocalDate> colDeadline;

    @FXML
    private TableColumn<Assignment, String> colDescription;

    Teacher teacher;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    private static Validation validation = new Validation();
    Logger LOGGER = Logger.getLogger(TeacherAssignmentsController.class.getName());

    private ObservableList<Assignment> assignmentsList = FXCollections.observableArrayList();
    private ObservableList<String> subjectCodes = FXCollections.observableArrayList();


    @FXML
    void delete(ActionEvent event) {
        Assignment assignment = assignmentsTable.getSelectionModel().getSelectedItem();
        assignmentService.remove(assignment);
        LOGGER.info("Assignment was removed");
        reset();
        updateTable();
        deleteAlert(assignment);

    }

    @FXML
    void save(ActionEvent event) {
        if (validation.validate("Title", title.getText(), "[a-zA-Z]+") &&
                validation.emptyValidation("Description", txtAreaDescription.getText().isEmpty()) &&
                validation.emptyValidation("Deadline", deadline.getEditor().getText().isEmpty()) &&
                validation.emptyValidation("Subject code", subjectCode.getSelectionModel().getSelectedItem() == null)) {

            if (assignmentId.getText().equals("") || assignmentId.getText() == null) {
                Assignment assignment = new Assignment();

                assignment.setName(title.getText());
                assignment.setDeadline(deadline.getValue());
                assignment.setDescription(txtAreaDescription.getText());
                assignment.setSubject(subjectService.findSubjectByCode(subjectCode.getValue()));
                assignment.setTeacher(teacher);

                assignmentService.persist(assignment);
                LOGGER.info("Assignment was persisted successfully");

                updateTable();
                reset();
                saveAlert(assignment);


            } else {
                Assignment existingAssignment = assignmentService.find(Integer.parseInt(assignmentId.getText()));

                existingAssignment.setName(title.getText());
                existingAssignment.setDeadline(deadline.getValue());
                existingAssignment.setDescription(txtAreaDescription.getText());
                existingAssignment.setSubject(subjectService.findSubjectByCode(subjectCode.getValue()));

                assignmentService.update(existingAssignment);
                LOGGER.info("Assignment was updated successfully");

                updateTable();
                reset();
                updateAlert(existingAssignment);

            }

        }
    }

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Teacher was logged out");
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
    void toSubjects(ActionEvent event) {
        stageManager.switchScene(FxmlView.TEACHER_SUBJECTS);
    }

    private void deleteAlert(Assignment a) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful delete");
        alert.setHeaderText(null);
        alert.setContentText("The assignment with the id   " + a.getId() +" was deleted successfully");
        alert.showAndWait();
    }

    private void saveAlert(Assignment a){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful save");
        alert.setHeaderText(null);
        alert.setContentText("The assignment with the id  "+ a.getId() +" has been created.");
        alert.showAndWait();
    }


    private void updateAlert(Assignment a){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful update");
        alert.setHeaderText(null);
        alert.setContentText("The assignment with the id  "+ a.getId() +" has been updated.");
        alert.showAndWait();
    }

    private void reset() {
        label.setText("Add new exam");
        save.setText("Save");
        assignmentId.setText(null);
        subjectCode.getSelectionModel().clearSelection();
        deadline.getEditor().clear();
        title.clear();
        txtAreaDescription.clear();
    }

    private void setColumnProperties(){
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colSubject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject().getCode()));
        colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colDeadline.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDeadline()));
        colDescription.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

    }

    private void updateTable() {
        loadAssignmentsDetails();
    }

    private void loadSubjectCodesToCheckBox() {
        subjectCodes.clear();
        List<Subject> subjects = teacher.getSubjects();
        for (Subject subject : subjects) {
            subjectCodes.add(subject.getCode());
        }

        subjectCode.setItems(subjectCodes);

    }

    private void loadAssignmentsDetails() {
        assignmentsList.clear();
        assignmentsList.addAll(assignmentService.findAssignmentsByTeacherId(teacher.getId()));
        assignmentsTable.setItems(assignmentsList);
    }

    private void fillTextFields() {
        reset();
        label.setText("Edit assignment");
        save.setText("Edit");
        Assignment assignment = assignmentsTable.getSelectionModel().getSelectedItem();
        assignmentId.setText(assignment.getId().toString());
        subjectCode.setValue(assignment.getSubject().getCode());
        deadline.setValue(assignment.getDeadline());
        title.setText(assignment.getName());
        txtAreaDescription.setText(assignment.getDescription());

    }

    /**
     * Initializing scene, sets column properties in the table,
     * loads list of the assignments and sets subjects codes to the check box
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacher = teacherService.findByUsername(LoginController.authorizationLogin);
        setColumnProperties();
        loadAssignmentsDetails();
        loadSubjectCodesToCheckBox();

        assignmentsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillTextFields();
//                assignmentsTable.getSelectionModel().clearSelection();
            }
        });

        reset.setOnAction(event -> reset());
        LOGGER.info("Canvas was initialized");
    }
}
