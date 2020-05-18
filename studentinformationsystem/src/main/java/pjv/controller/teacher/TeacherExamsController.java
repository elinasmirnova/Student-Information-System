package pjv.controller.teacher;

import com.jfoenix.controls.JFXTimePicker;
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
import org.springframework.stereotype.Controller;
import pjv.controller.LoginController;
import pjv.model.*;
import pjv.service.ExamService;
import pjv.service.SubjectService;
import pjv.service.TeacherService;
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

@Controller
public class TeacherExamsController implements Initializable {

    @FXML
    private MenuItem homeMenuItem;

    @FXML
    private MenuItem subjectsMenuItem;

    @FXML
    private MenuItem homeMenuItem1;

    @FXML
    private Menu assignmentsMenuItem;

    @FXML
    private Label label;

    @FXML
    private Label examId;

    @FXML
    private ComboBox<String> subjectCode;

    @FXML
    private DatePicker date;

    @FXML
    private TextField capacity;

    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private RadioButton rbAvailable;

    @FXML
    private RadioButton rbNotAvailable;

    @FXML
    private ComboBox<String> classroom;

    @FXML
    private Button reset;

    @FXML
    private Button saveExam;

    @FXML
    private Button btnLogout;

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
    private MenuItem deleteUsers;

    @Autowired
    private ExamService examService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private SubjectService subjectService;

    Teacher teacher;

    private ObservableList<Exam> examsList = FXCollections.observableArrayList();
    private ObservableList<String> subjectCodes = FXCollections.observableArrayList();
    private ObservableList<String> classroomList = FXCollections.observableArrayList("T2:D309", "K:E107", "T2:D209", "T2:D337" );

    @FXML
    private MenuItem deleteExam;

    @FXML
    void deleteExam(ActionEvent event) {
        Exam examToRemove = examsTable.getSelectionModel().getSelectedItem();
        examService.remove(examToRemove);
        reset();
        updateTable();
    }

    @FXML
    void save(ActionEvent event) throws ParseException {
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
            updateTable();


        } else {
            Exam exam = examService.find(Integer.parseInt(examId.getText()));
            exam.setCapacity(Integer.parseInt(capacity.getText()));
            exam.setClassroom(classroom.getValue());
            exam.setAvailable(rbAvailable.isSelected());
            exam.setDate(date.getValue());
            exam.setTime(timePicker.getValue().toString());
            examService.update(exam);
            updateTable();

        }
    }

    @FXML
    void toAssignments(ActionEvent event) {

    }

    @FXML
    void toHome(ActionEvent event) {

    }

    @FXML
    void toSubjects(ActionEvent event) {

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

    public void updateTable() {
        loadSubjectsDetails();
    }

    public void loadSubjectsDetails() {
        examsList.clear();
        examsList.addAll(examService.findExamsByTeacherId(teacher.getId()));
        examsTable.setItems(examsList);
    }

    private void loadSubjectCodesToCheckBox() {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        teacher = teacherService.findByUsername(LoginController.authorizationLogin);
        setColumnProperties();
        setColumnAvailableProperties();
        classroom.setItems(classroomList);
        loadSubjectCodesToCheckBox();
        loadSubjectsDetails();

        examsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillTextFields();
                //userTable.getSelectionModel().clearSelection();
            }
        });

        reset.setOnAction(event -> reset());
    }
}
