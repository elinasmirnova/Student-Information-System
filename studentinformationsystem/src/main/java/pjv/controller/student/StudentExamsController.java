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
    private TableView<Exam> examsToEnrollTable;

    @FXML
    private TableColumn<Exam, LocalDate> dateColumn1;

    @FXML
    private TableColumn<Exam, String> timeColumn1;

    @FXML
    private TableColumn<Exam, String> classroomColumn1;

    @FXML
    private TableColumn<Exam, Integer> capacityColumn1;

    @FXML
    private TableColumn<Exam, Integer> occupiedColumn1;

    @FXML
    private TableColumn<Exam, String> enrollColumn1;

    @FXML
    private JFXComboBox<String> comboBox;

    @FXML
    private TableView<Exam> enrolledExamsTable;

    @FXML
    private TableColumn<Exam, LocalDate> dateColumn2;

    @FXML
    private TableColumn<Exam, String> timeColumn2;

    @FXML
    private TableColumn<Exam, String> classroomColumn2;

    @FXML
    private TableColumn<Exam, Integer> capacityColumn2;

    @FXML
    private TableColumn<Exam, Integer> occupiedColumn2;

    @FXML
    private TableColumn<Exam, String> withdrawColumn;

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

    private ObservableList<Exam> examsToEnroll = FXCollections.observableArrayList();
    private ObservableList<Exam> enrolledExamsList = FXCollections.observableArrayList();
    private ObservableList<String> subjectCodesList = FXCollections.observableArrayList();


    @FXML
    void clear(ActionEvent event) {
        examsToEnroll.clear();
        enrolledExamsList.clear();
        comboBox.getSelectionModel().clearSelection();
    }

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Student was logged out");
    }

    @FXML
    void submit(ActionEvent event) {
        //List<Exam> foundExams = new ArrayList<>();
        if (comboBox.getSelectionModel().getSelectedItem() != null) {
            String subjectCode = comboBox.getSelectionModel().getSelectedItem();
            updateTables(subjectCode);
//            examsToEnroll.addAll(examService.findAvailableToEnrollExamsBySubject(subjectCode));
//
//            List<Exam> enrolledExams = student.getExams();
//
//            examsToEnroll.removeAll(enrolledExams);

//            for (Exam e: enrolledExams) {
//                examsToEnroll.remove(e);
//            }
           // List<Exam> studentsExams = student.getExams();
//            System.out.println(student.getExams());
//            System.out.println(examsList);
//            for (Exam e: studentsExams) {
//                examsList.remove(e);
//            }

//            for (Exam e: studentsExams) {
//                if (e.getSubject().getCode().equals(subjectCode)) {
//                    examsList.add(e);
//                }
            }
//            examsToEnrollTable.setItems(examsToEnroll);

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

    private void setColumnPropertiesForTable1(){
        dateColumn1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        timeColumn1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        classroomColumn1.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassroom()));
        capacityColumn1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCapacity()));
        occupiedColumn1.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOccupied()));
        Callback<TableColumn<Exam, String>, TableCell<Exam, String>> cellFactory
                =
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

                                            //Exam exam = examsToEnrollTable.getSelectionModel().getSelectedItem();
                                            Exam exam = (Exam) getTableRow().getItem();
                                            exam.setOccupied(exam.getOccupied()+1);
                                            student.addExam(exam);
                                            exam.addStudent(student);
                                            studentService.update(student);
                                            examService.update(exam);

                                            String subjectCode = comboBox.getSelectionModel().getSelectedItem();
                                            updateTables(subjectCode);

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        enrollColumn1.setCellFactory(cellFactory);
        //colAvailable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isAvailable()));

    }

    private void updateTables(String subjectCode) {
        examsToEnroll.clear();
        enrolledExamsList.clear();

        List<Exam> allExams = new ArrayList<>(examService.findAvailableToEnrollExamsBySubject(subjectCode));

        List<Exam> enrolledExams = student.getExams();

        for (Exam e : enrolledExams) {
            if (e.getSubject().getCode().equals(subjectCode)) {
                enrolledExamsList.add(e);
            }
        }

//        System.out.println( (enrolledExams.size() != 0 ) ? enrolledExams.get(0).getId() : null);
//        System.out.println( (examsToEnroll.size() != 0 ) ? examsToEnroll.get(0).getId() : null);
//        System.out.println(enrolledExams);
//        System.out.println(examsToEnroll);
        if (enrolledExams.size() != 0) {
            for (Exam e1 : allExams) {
                for (Exam e2: enrolledExams) {
                    if (!e1.getId().equals(e2.getId())) {
                        examsToEnroll.add(e1);
                    }
                }
            }
        } else {
            examsToEnroll.addAll(allExams);
        }
//        System.out.println(enrolledExams);
//        System.out.println(examsToEnroll);
//        System.out.println((enrolledExams.size() != 0  ) ?enrolledExams.get(0).getId() : null );
//        System.out.println((examsToEnroll.size() != 0 ) ? examsToEnroll.get(0).getId() : null);


        enrolledExamsTable.setItems(enrolledExamsList);
        examsToEnrollTable.setItems(examsToEnroll);
    }

    private void setColumnPropertiesForTable2(){
        dateColumn2.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));
        timeColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTime()));
        classroomColumn2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClassroom()));
        capacityColumn2.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCapacity()));
        occupiedColumn2.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getOccupied()));
        Callback<TableColumn<Exam, String>, TableCell<Exam, String>> cellFactory
                =
                new Callback<TableColumn<Exam, String>, TableCell<Exam, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Exam, String> param) {
                        final TableCell<Exam, String> cell = new TableCell<Exam, String>() {

                            final Button btn = new Button("Withdraw");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                            //Exam exam = enrolledExamsTable.getSelectionModel().getSelectedItem();
                                            Exam exam = (Exam) getTableRow().getItem();

                                            exam.setOccupied(exam.getOccupied()-1);
                                            student.removeExam(exam);
                                            exam.removeStudent(student);
                                            examService.update(exam);
                                            studentService.update(student);

                                            String subjectCode = comboBox.getSelectionModel().getSelectedItem();

                                            studentService.deleteStudentFromExam(student.getId(), exam.getId());


                                            updateTables(subjectCode);


                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        withdrawColumn.setCellFactory(cellFactory);
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
        setColumnPropertiesForTable1();
        setColumnPropertiesForTable2();
        LOGGER.info("Canvas was initialized");
    }
}
