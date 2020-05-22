package pjv.controller.teacher;

import com.jfoenix.controls.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.LoginController;
import pjv.controller.Validation;
import pjv.model.*;
import pjv.service.ExamService;
import pjv.service.SubjectService;
import pjv.service.TeacherService;
import pjv.view.FxmlView;
import sun.java2d.pipe.SpanShapeRenderer;

import java.net.URL;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the scene, where teacher is allowed to manages exams
 */
@Controller
public class TeacherExamsController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private Label examId;

    @FXML
    private JFXComboBox<String> subjectCode;

    @FXML
    private JFXDatePicker date;

    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private JFXTextField capacity;

    @FXML
    private JFXRadioButton rbAvailable;

    @FXML
    private JFXRadioButton rbNotAvailable;

    @FXML
    private JFXComboBox<String> classroom;

    @FXML
    private JFXButton reset;

    @FXML
    private JFXButton saveExam;

    @FXML
    private TableView<Exam> examsTable;

    @FXML
    private TableColumn<Exam, Integer> colId;

    @FXML
    private TableColumn<Exam, Subject> colSubject;

    @FXML
    private TableColumn<Exam, LocalDate> colDate;

    @FXML
    private TableColumn<Exam, String> colTime;

    @FXML
    private TableColumn<Exam, Integer> colCapacity;

    @FXML
    private TableColumn<Exam, Integer> colOccupied;

    @FXML
    private TableColumn<Exam, String> colClassroom;

    @FXML
    private TableColumn<Exam, Boolean> colAvailable;

    @FXML
    private MenuItem deleteExam;

    @Autowired
    private ExamService examService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    @Lazy
    @Autowired
    private StageManager stageManager;

    Teacher teacher;

    Logger LOGGER = Logger.getLogger(TeacherExamsController.class.getName());

    private static Validation validation = new Validation();

    private ObservableList<Exam> examsList = FXCollections.observableArrayList();
    private ObservableList<String> subjectCodes = FXCollections.observableArrayList();
    private ObservableList<String> classroomList = FXCollections.observableArrayList("T2:D309", "K:E107", "T2:D209", "T2:D337" );


    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Teacher was logged out");
    }

    @FXML
    void deleteExam(ActionEvent event) {
        Exam examToRemove = examsTable.getSelectionModel().getSelectedItem();
        examService.remove(examToRemove);
        LOGGER.info("Exam was removed successfully");
        reset();
        updateTable();
        deleteAlert(examToRemove);
    }

    @FXML
    void save(ActionEvent event) throws ParseException {

            if (validation.validate("capacity", capacity.getText(), "[1-100]") &&
                validation.emptyValidation("subject code", subjectCode.getSelectionModel().getSelectedItem() == null) &&
                validation.emptyValidation("date", date.getEditor().getText().isEmpty()) &&
                validation.emptyValidation("time", timePicker.getEditor().getText().isEmpty()) &&
                (rbAvailable.isSelected() || rbNotAvailable.isSelected()) &&
        validation.emptyValidation("classroom", classroom.getSelectionModel().getSelectedItem() == null)) {

            if (examId.getText().equals("") || examId.getText() == null) {
                Exam exam = new Exam();
                exam.setCapacity(Integer.parseInt(capacity.getText()));
                exam.setClassroom(classroom.getValue());
                exam.setAvailable(rbAvailable.isSelected());
                exam.setDate(date.getValue());
                exam.setTime(timePicker.getValue().toString());
                exam.setSubject(subjectService.findSubjectByCode(subjectCode.getValue()));
                exam.setTeacher(teacher);
                examService.persist(exam);
                LOGGER.info("Exam was persisted successfully");
                reset();
                updateTable();
                saveAlert(exam);


            } else {
                Exam exam = examService.find(Integer.parseInt(examId.getText()));
                exam.setCapacity(Integer.parseInt(capacity.getText()));
                exam.setClassroom(classroom.getValue());
                exam.setAvailable(rbAvailable.isSelected());
                exam.setDate(date.getValue());
                exam.setTime(timePicker.getValue().toString());
                examService.update(exam);
                LOGGER.info("Exam was updated");
                reset();
                updateTable();
                updateAlert(exam);

            }
        }
    }

    @FXML
    void toAssignments(ActionEvent event) {
        stageManager.switchScene(FxmlView.TEACHER_ASSIGNMENTS);
    }

    @FXML
    void toHome(ActionEvent event) {
        stageManager.switchScene(FxmlView.TEACHER_MAIN);
    }

    @FXML
    void toSubjects(ActionEvent event) {
        stageManager.switchScene(FxmlView.TEACHER_SUBJECTS);
    }

    private void deleteAlert(Exam e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful delete");
        alert.setHeaderText(null);
        alert.setContentText("The exam with the id   " + e.getId() +" was deleted successfully");
        alert.showAndWait();
    }

    private void saveAlert(Exam e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful save");
        alert.setHeaderText(null);
        alert.setContentText("The exam with the id  "+ e.getId() +" has been created.");
        alert.showAndWait();
    }

    private void updateAlert(Exam e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful update");
        alert.setHeaderText(null);
        alert.setContentText("The exam with the id  "+ e.getId() +" has been updated.");
        alert.showAndWait();
    }

    private void reset() {
        label.setText("Add new exam");
        saveExam.setText("Save");
        examId.setText(null);
        subjectCode.getSelectionModel().clearSelection();
        date.getEditor().clear();
        capacity.clear();
        timePicker.setValue(null);
        rbAvailable.setSelected(false);
        rbNotAvailable.setSelected(false);
        classroom.getSelectionModel().clearSelection();
    }

    private void setColumnProperties(){
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colSubject.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getSubject().getCode()));
        colDate.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getDate()));
        colTime.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTime()));
        colCapacity.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCapacity()));
        colOccupied.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOccupied()));
        colClassroom.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassroom()));
        //colAvailable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isAvailable()));

    }

    private void setColumnAvailableProperties() {
        colAvailable.setCellValueFactory(param -> {
            Exam exam = param.getValue();
            SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(exam.isAvailable());

            booleanProperty.addListener((observable, oldValue, newValue) -> {
                exam.setAvailable(newValue);
                examService.update(exam);
            });
            return booleanProperty;
        });

        colAvailable.setCellFactory(param -> {
            CheckBoxTableCell<Exam, Boolean> cell = new CheckBoxTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void updateTable() {
        loadExamsDetails();
    }

    private void loadExamsDetails() {
        examsList.clear();
        examsList.addAll(examService.findExamsByTeacherId(teacher.getId()));
        examsTable.setItems(examsList);
    }

    private void loadSubjectCodesToCheckBox() {
        subjectCodes.clear();
        List<Subject> subjects = teacher.getSubjects();
        for (Subject subject : subjects) {
            subjectCodes.add(subject.getCode());
        }

        subjectCode.setItems(subjectCodes);

    }

    private void fillTextFields() {
        reset();
        label.setText("Edit exam info");
        saveExam.setText("Edit");
        Exam selectedExam = examsTable.getSelectionModel().getSelectedItem();
        examId.setText(selectedExam.getId().toString());
        subjectCode.setValue(selectedExam.getSubject().getCode());
        date.setValue(selectedExam.getDate());
        capacity.setText(selectedExam.getCapacity().toString());
        timePicker.setValue(LocalTime.parse(selectedExam.getTime(), DateTimeFormatter.ofPattern("HH:mm")));
        if (selectedExam.isAvailable()) {
            rbAvailable.setSelected(true);
        } else {
            rbNotAvailable.setSelected(true);
        }
        classroom.setValue(selectedExam.getClassroom());

    }

    /**
     * Initializing scene, sets column properties in the table,
     * loads and sets list of the exams to the table,
     * sets subjects codes to the check box
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacher = teacherService.findByUsername(LoginController.authorizationLogin);
        setColumnProperties();
        setColumnAvailableProperties();
        classroom.setItems(classroomList);
        loadSubjectCodesToCheckBox();
        loadExamsDetails();

        examsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillTextFields();
                //userTable.getSelectionModel().clearSelection();
            }
        });

        reset.setOnAction(event -> reset());
        LOGGER.info("Canvas was initialized");
    }
}
