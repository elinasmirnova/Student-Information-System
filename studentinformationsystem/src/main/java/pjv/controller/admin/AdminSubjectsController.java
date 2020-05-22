package pjv.controller.admin;


import com.jfoenix.controls.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.Validation;
import pjv.model.Student;
import pjv.model.Subject;
import pjv.model.Teacher;
import pjv.service.SubjectService;
import pjv.service.TeacherService;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Controller for the scene, where admin can manages subjects list
 */
@Controller
public class AdminSubjectsController implements Initializable {

    @FXML
    private Label mainLabel;

    @FXML
    private Label subjectId;

    @FXML
    private JFXTextField code;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXRadioButton rbSummer;

    @FXML
    private JFXRadioButton rbWinter;

    @FXML
    private JFXComboBox<Integer> credits;

    @FXML
    private JFXComboBox<String> role;

    @FXML
    private CheckComboBox<String> teachersTest1;

    @FXML
    private JFXTextArea txtAreaSynopsis;

    @FXML
    private JFXButton reset;

    @FXML
    private JFXButton saveSubject;

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

    Logger LOGGER = Logger.getLogger(AdminSubjectsController.class.getName());

    private static Validation validation = new Validation();

    private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();
    private ObservableList<String> teachersList = FXCollections.observableArrayList();
    private ObservableList<Integer> creditsList = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 25);
    private ObservableList<String> rolesList = FXCollections.observableArrayList("(P) Compulsory", "(V) Elective", "(PV)");

    @FXML
    void toHome(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_MAIN);
    }

    @FXML
    void toStudents(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_STUDENTS);
    }

    @FXML
    void toTeachers(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_TEACHERS);
    }

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void deleteSubject(ActionEvent event) {
        Subject subjectToDelete = subjectsTable.getSelectionModel().getSelectedItem();
        subjectService.remove(subjectToDelete);
        LOGGER.info("Subject was removed successfully");
        reset();
        updateTable();
        deleteAlert(subjectToDelete);
    }

    @FXML
    void save(ActionEvent event) {
        if (validation.validate("Code", code.getText(), "[a-zA-Z0-9]+") &&
                validation.validate("Name", name.getText(), "[a-zA-Z]+") &&
              //  validation.validate("Synopsis", txtAreaSynopsis.getText(), "([A-Za-z0-9]+\\.[A-Za-z0-9]+(\\r)?(\\n)?)") &&
                validation.emptyValidation("Credits", credits.getSelectionModel().getSelectedItem() == null) &&
                validation.emptyValidation("Role", role.getSelectionModel().getSelectedItem() == null) &&
                (rbSummer.isSelected()) || rbWinter.isSelected() ) {

            if (subjectId.getText().equals("") || subjectId.getText() == null) {
                if (!subjectService.exists(code.getText())) {
                    Subject subject = new Subject();
                    subject.setCode(code.getText());
                    subject.setName(name.getText());
                    subject.setCredits(credits.getValue());
                    subject.setRole(role.getValue());
                    subject.setSemester(rbSummer.isSelected() ? "Summer" : "Winter");
                    subject.setSynopsis(txtAreaSynopsis.getText());
                    List<Teacher> checkedTeachers = getCheckedTeachers();
                    subject.setTeachers(checkedTeachers);
                    subjectService.persist(subject);
                    LOGGER.info("Subject entity was persisted");
                    // setSubjectToTeachers(checkedTeachers, subject);
                    updateTable();
                    reset();
                    saveAlert(subject);
                } else {
                    subjectAlreadyExists();
                }
            } else {
                Subject subject = subjectService.find(Integer.parseInt(subjectId.getText()));
                subject.setCode(code.getText());
                subject.setName(name.getText());
                subject.setCredits(credits.getValue());
                subject.setRole(role.getValue());
                subject.setSemester(rbSummer.isSelected() ? "Summer" : "Winter");
                subject.setSynopsis(txtAreaSynopsis.getText());
                List<Teacher> checkedTeachers = getCheckedTeachers();
                subject.setTeachers(checkedTeachers);
                subjectService.update(subject);

                LOGGER.info("Subject entity was updated successfully");

                updateTable();
                reset();
                updateAlert(subject);

            }

        }

        }

    private void subjectAlreadyExists() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("Subject with this code already exists. Please enter a different code");
        alert.showAndWait();
    }

    private void deleteAlert(Subject subject) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful delete");
        alert.setHeaderText(null);
        alert.setContentText("The subject with the code  " + subject.getCode() +" was deleted successfully");
        alert.showAndWait();
    }

    private void saveAlert(Subject subject){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful save");
        alert.setHeaderText(null);
        alert.setContentText("The subject with the code "+ subject.getCode() +" has been created.");
        alert.showAndWait();
    }


    private void updateAlert(Subject subject){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful update");
        alert.setHeaderText(null);
        alert.setContentText("The subject with the code  "+ subject.getCode() +" has been updated.");
        alert.showAndWait();
    }

    public void setSubjectToTeachers(List<Teacher> teachers, Subject subject) {
        for (Teacher teacher : teachers) {
            teacher.addSubject(subject);
            teacherService.update(teacher);
        }

    }

    private void getAllTeachersList() {
        teachersList.clear();
        StringBuilder builder;
        List<Teacher> list = teacherService.findAll();
        for (Teacher teacher : list ) {
            builder = new StringBuilder();
            builder.append("id - ")
                    .append(teacher.getId())
                    .append(", ")
                    .append(teacher.getFirstName())
                    .append(" ")
                    .append(teacher.getLastName());
            System.out.println(builder.length());
            teachersList.add(builder.toString());
        }
    }

    /**
     * Fills combo box with teachers list
     */
    private void fillComboBoxTeachers() {
        getAllTeachersList();
        teachersTest1.getItems().clear();
        //teachersTest2.setItems(teachersList);
        teachersTest1.getItems().addAll(teachersList);
    }

    /**
     * Returns selected teachers from combo box in list
     * @return
     */
    private List<Teacher> getCheckedTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        ObservableList<String> checkedTeachersString = teachersTest1.getCheckModel().getCheckedItems();
        for (String teacherString : checkedTeachersString) {
            Teacher teacher = teacherService.find(Character.getNumericValue(teacherString.charAt(5)));
            teachers.add(teacher);
        }
        return teachers;
    }

    /**
     * Clear text fields
     */
    private void reset() {
        subjectId.setText(null);
        mainLabel.setText("Add New Subject");
        saveSubject.setText("Save");
        code.clear();
        name.clear();
        credits.getSelectionModel().clearSelection();
        role.getSelectionModel().clearSelection();
        rbSummer.setSelected(false);
        rbWinter.setSelected(false);
        txtAreaSynopsis.clear();
        teachersTest1.getCheckModel().clearChecks();
    }

    /**
     * Fills table with subjects
     */
    private void loadSubjectsDetails() {
        subjectsTable.getItems().clear();
        subjectsList.clear();
        subjectsList.addAll(subjectService.findAll());
        subjectsTable.setItems(subjectsList);
    }

    private void setColumnProperties(){
        colUserId.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getId()));
        colCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colCredits.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCredits()));
        colRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        colSemester.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemester()));

    }

    /**
     * Refreshes data in the table
     */
    private void updateTable() {
        loadSubjectsDetails();
    }

    /**
     * Fill the text fields with the information about selected subject
     */
    private void fillTextFields() {
        reset();
        mainLabel.setText("Edit subject");
        saveSubject.setText("Edit");
        Subject subject = subjectsTable.getSelectionModel().getSelectedItem();
        subjectId.setText(subject.getId().toString());
        code.setText(subject.getCode());
        name.setText(subject.getName());
        credits.setValue(subject.getCredits());
        role.setValue(subject.getRole());
        txtAreaSynopsis.setText(subject.getSynopsis());
        if (subject.getSemester().equals("Summer")) {
            rbSummer.setSelected(true);
        } else {
            rbWinter.setSelected(true);
        }
        teachersTest1.getCheckModel().clearChecks();
        List<Integer> idsTeachers = extractTeachers(subject);
        for (Integer id : idsTeachers) {
            teachersTest1.getItems()
                                    .forEach( item -> {
                                        if (Character.getNumericValue(item.charAt(5)) == id) {
                                            teachersTest1.getCheckModel().check(item);
                                        }
                                    }
                );
        }
    }

    private List<Integer> extractTeachers(Subject subject) {
        List<Integer> idsTeachers = new ArrayList<>();
        subjectService.find(subject.getId()).getTeachers().forEach( t -> idsTeachers.add(t.getId()));
       //subject.getTeachers().forEach( t -> idsTeachers.add(t.getId()));
        return idsTeachers;
    }

    /**
     * Initializing scene, sets column properties in the table, loads subject list and sets it to the table,
     * fills combo box with the teachers list
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        role.setItems(rolesList);
        credits.setItems(creditsList);
        setColumnProperties();
        loadSubjectsDetails();
        fillComboBoxTeachers();
        subjectsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillTextFields();
                //userTable.getSelectionModel().clearSelection();
            }
        });

        reset.setOnAction(event -> reset());
        LOGGER.info("Canvas was rendered");
    }
}
