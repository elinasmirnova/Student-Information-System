package pjv.controller.admin;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
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
import pjv.controller.Validation;
import pjv.model.Student;
import pjv.model.User;
import pjv.service.StudentService;
import pjv.service.UserService;
import pjv.view.FxmlView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Controller
public class AdminStudentsController implements Initializable {

    @FXML
    private Label userId;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXDatePicker dob;

    @FXML
    private JFXComboBox<Integer> year;

    @FXML
    private JFXComboBox<String> studyProgram;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton reset;

    @FXML
    private TableView<Student> userTable;

    @FXML
    private TableColumn<Student, Integer> colUserId;

    @FXML
    private TableColumn<Student, String> colUsername;

    @FXML
    private TableColumn<Student, String> colFirstName;

    @FXML
    private TableColumn<Student, String> colLastName;

    @FXML
    private TableColumn<Student, LocalDate> colDoB;

    @FXML
    private TableColumn<Student, Integer> colYear;

    @FXML
    private TableColumn<Student, String> colStudyProgram;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private StudentService studentService;

    @Autowired
    private UserService userService;

    private static Validation validation = new Validation();

    private ObservableList<Student> userList = FXCollections.observableArrayList();
    private ObservableList<Integer> years = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);
    private ObservableList<String> programs = FXCollections.observableArrayList("Applied Informatics", "Software engineering", "Cybernetics");


    public void reset() {
        userId.setText(null);
        username.clear();
        username.setDisable(false);
        firstName.clear();
        lastName.clear();
        dob.getEditor().clear();
        year.getSelectionModel().clearSelection();
        studyProgram.getSelectionModel().clearSelection();
        password.clear();
        password.setDisable(false);
        userTable.getSelectionModel().clearSelection();
    }

    @FXML
    void toHome(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_MAIN);
    }

    @FXML
    void toSubjects(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_SUBJECTS);
    }

    @FXML
    void toTeachers(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_TEACHERS);
    }

    @FXML
    void deleteUser(ActionEvent event) {
        Student studentToRemove = userTable.getSelectionModel().getSelectedItem();
        studentService.remove(studentToRemove);
        userService.remove(studentToRemove.getUser());
        reset();
        updateTable();
        deleteAlert(studentToRemove);

    }

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    @FXML
    void save(ActionEvent event) {

        if (validation.validate("First Name", firstName.getText(), "[a-zA-Z]+") &&
                validation.validate("Last Name", lastName.getText(), "[a-zA-Z]+") &&
                validation.emptyValidation("Date of Birth", dob.getEditor().getText().isEmpty()) &&
                validation.emptyValidation("Year", year.getSelectionModel().getSelectedItem() == null) &&
                validation.emptyValidation("Study program", studyProgram.getSelectionModel().getSelectedItem() == null)) {

            if (userId.getText() == null || userId.getText().equals("")) {
//                if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
//                        emptyValidation("Password", getPassword().isEmpty())){

                if (validation.emptyValidation("Password", password.getText().isEmpty())
                    && validation.validate("Username", username.getText(), "[a-zA-Z0-9]+")) {
                     if (!userService.ifExists(username.getText())) {

                         Student student = new Student();
                         student.setFirstName(firstName.getText());
                         User user = new User();
                         user.setRole("student");
                         user.setUsername(username.getText());
                         user.setPassword(password.getText());
                         userService.persist(user);

                         student.setUser(user);
                         student.setLastName(lastName.getText());
                         student.setDateOfBirth(dob.getValue());
                         student.setStudyProgram(studyProgram.getValue());
                         student.setYear(year.getValue());
                         studentService.persist(student);
                         reset();

                         updateTable();
                         saveAlert(student);
                     } else {
                         usernameAlreadyExists();
                     }

                }

            } else {

                Student student = studentService.find(Integer.parseInt(userId.getText()));

                student.setFirstName(firstName.getText());
                student.setLastName(lastName.getText());
                student.setDateOfBirth(dob.getValue());
                student.setStudyProgram(studyProgram.getValue());
                student.setYear(year.getValue());
                studentService.update(student);
                reset();

                updateTable();
                updateAlert(student);

            }

        }
    }


    private void deleteAlert(Student student) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful delete");
        alert.setHeaderText(null);
        alert.setContentText("The student with the id  " + student.getId() +" was deleted successfully");
        alert.showAndWait();
    }

    private void saveAlert(Student student){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful save");
        alert.setHeaderText(null);
        alert.setContentText("The student with the id  "+ student.getId() +" has been created.");
        alert.showAndWait();
    }

    private void updateAlert(Student student){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful update");
        alert.setHeaderText(null);
        alert.setContentText("The student with the id  "+ student.getId() +" has been updated.");
        alert.showAndWait();
    }

    private void usernameAlreadyExists() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("User with this username already exists. Please use a different username");
    }


    private void loadUserDetails() {
        userList.clear();
        userList.addAll(studentService.findAll());
        userTable.setItems(userList);
    }

    private void setColumnProperties(){
        colUserId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));
        colFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        colLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        colDoB.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateOfBirth()));
        colYear.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getYear()));
        colStudyProgram.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudyProgram()));

    }

    public void fillTextFields() {
        Student student = userTable.getSelectionModel().getSelectedItem();
        password.setDisable(true);
        userId.setText(student.getId().toString());
        username.setText(student.getUser().getUsername());
        username.setDisable(true);
        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());
        dob.setValue(student.getDateOfBirth());
        year.setValue(student.getYear());
        studyProgram.setValue(student.getStudyProgram());
        password.setDisable(true);

    }

    public void updateTable() {
        loadUserDetails();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        year.setItems(years);
        studyProgram.setItems(programs);
        setColumnProperties();
        loadUserDetails();
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillTextFields();
                //userTable.getSelectionModel().clearSelection();
            }
        });

        reset.setOnAction(event -> reset());

    }
}
