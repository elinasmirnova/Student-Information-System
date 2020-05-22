package pjv.controller.student;

import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.LoginController;
import pjv.model.*;
import pjv.service.EnrolledStudentService;
import pjv.service.ExamService;
import pjv.service.StudentService;
import pjv.service.SubjectService;
import pjv.view.FxmlView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the scene, where student can look through the available exams and enrolled to them.
 */
@Controller
public class StudentExamsController implements Initializable {

    @FXML
    private TableView<Exam> examsTable;

    @FXML
    private TableColumn<Exam, LocalDate> dateColumn;

    @FXML
    private TableColumn<Exam, String> timeColumn;

    @FXML
    private TableColumn<Exam, String> classroomColumn;

    @FXML
    private TableColumn<Exam, Integer> capacityColumn;

    @FXML
    private TableColumn<Exam, Integer> occupiedColumn;

    @FXML
    private TableColumn<Exam, String> enrollColumn;

    @FXML
    private JFXComboBox<String> comboBox;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EnrolledStudentService enrolledStudentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ExamService examService;

    Student student;

    Logger LOGGER = Logger.getLogger(StudentExamsController.class.getName());

    private ObservableList<Exam> examsList = FXCollections.observableArrayList();
    private ObservableList<String> subjectCodesList = FXCollections.observableArrayList();


    @FXML
    void clear(ActionEvent event) {
        examsList.clear();
        comboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Student was logged out");
    }

    @FXML
    void submit(ActionEvent event) {
        examsList.clear();
        if (comboBox.getSelectionModel().getSelectedItem() != null) {
            String subjectCode = comboBox.getSelectionModel().getSelectedItem();
            examsList.addAll(examService.findAvailableToEnrollExamsBySubject(subjectCode));
            List<Exam> studentsExams = student.getExams();
//            System.out.println(student.getExams());
//            System.out.println(examsList);
            for (Exam e: studentsExams) {
                examsList.remove(e);
            }
            examsTable.setItems(examsList);
        }
    }


    @FXML
    void toAssignments(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_ASSIGNMENTS);
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
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        timeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        classroomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassroom()));
        capacityColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCapacity()));
        occupiedColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOccupied()));
        Callback<TableColumn<Exam, String>, TableCell<Exam, String>> cellFactory
                = //
                new Callback<TableColumn<Exam, String>, TableCell<Exam, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Exam, String> param) {
                        final TableCell<Exam, String> cell = new TableCell<Exam, String>() {

                            final Button btn = new Button("Enroll");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        if (btn.getText().equals("Enroll")) {
                                            Exam exam = examsTable.getSelectionModel().getSelectedItem();
                                            exam.setOccupied(exam.getOccupied()+1);
                                            student.addExam(exam);
                                            exam.addStudent(student);
                                            examService.update(exam);
                                            studentService.update(student);
                                            String subjectCode = comboBox.getSelectionModel().getSelectedItem();
                                            examsList.clear();
                                            examsList.addAll(examService.findAvailableToEnrollExamsBySubject(subjectCode));
                                            examsTable.setItems(examsList);

                                            btn.setText("Withdraw");
                                        } else {
                                            Exam exam = examsTable.getSelectionModel().getSelectedItem();
                                            exam.setOccupied(exam.getOccupied()-1);
                                            student.removeExam(exam);
                                            exam.removeStudent(student);
                                            examService.update(exam);
                                            studentService.update(student);
                                            btn.setText("Enroll");
                                        }

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        enrollColumn.setCellFactory(cellFactory);
        //colAvailable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isAvailable()));

    }

    private void loadSubjects() {
        comboBox.getItems().clear();
        subjectCodesList.clear();
        List<Integer> subjectIDs = enrolledStudentService.findSubjectsIDByStudentId(student.getId());
//        List<Subject> subjects = new ArrayList<>();
        for (Integer id : subjectIDs) {
            subjectCodesList.add(subjectService.find(id).getCode());
        }
        comboBox.setItems(subjectCodesList);

    }

    /**
     * Initializing scene, loads subjects list to the combo box,
     * sets column properties in the table.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        student = studentService.findByUsername(LoginController.authorizationLogin);
        loadSubjects();
        setColumnProperties();
        LOGGER.info("Canvas was initialized");
    }
}
