package pjv.controller.admin;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
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
import pjv.model.Teacher;
import pjv.model.User;
import pjv.service.TeacherService;
import pjv.service.UserService;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AdminTeachersController implements Initializable {

    @FXML
    private Label userId;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXComboBox<String> degree;

    @FXML
    private JFXComboBox<String> department;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton reset;

    @FXML
    private TableView<Teacher> userTable;

    @FXML
    private TableColumn<Teacher, Integer> colUserId;

    @FXML
    private TableColumn<Teacher, String> colUsername;

    @FXML
    private TableColumn<Teacher, String> colDegree;

    @FXML
    private TableColumn<Teacher, String> colDepartment;

    @FXML
    private TableColumn<Teacher, String> colFirstName;

    @FXML
    private TableColumn<Teacher, String> colLastName;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private UserService userService;

    private Validation validation;

    private ObservableList<Teacher> teachersList = FXCollections.observableArrayList();
    private ObservableList<String> degrees = FXCollections.observableArrayList("Bc.", "Mgr.", "Ing.", "RNDr.", "Ph.D.");
    private ObservableList<String> departments = FXCollections.observableArrayList("Computer Science", "Cybernetics", "Software engineering", "Mathematics");

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }


    @FXML
    void toHome(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_MAIN);
    }

    @FXML
    void toStudents(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_STUDENTS);
    }

    @FXML
    void toSubjects(ActionEvent event) {
        stageManager.switchScene(FxmlView.ADMIN_SUBJECTS);
    }

    public void reset() {
        userId.setText(null);
        username.clear();
        username.setDisable(false);
        firstName.clear();
        lastName.clear();
        degree.getSelectionModel().clearSelection();
        department.getSelectionModel().clearSelection();
        password.clear();
        password.setDisable(false);
        userTable.getSelectionModel().clearSelection();
    }
    @FXML
    void deleteUser(ActionEvent event) {
        Teacher teacherToRemove = userTable.getSelectionModel().getSelectedItem();
        teacherService.remove(teacherToRemove);
        userService.remove(teacherToRemove.getUser());
        reset();
        updateTable();
        deleteAlert(teacherToRemove);
    }

    @FXML
    void save(ActionEvent event) {

        if (validation.validate("First Name", firstName.getText(), "[a-zA-Z]+") &&
                validation.validate("Last Name", lastName.getText(), "[a-zA-Z]+") &&
                validation.emptyValidation("Degree", degree.getSelectionModel().getSelectedItem() == null) &&
                validation.emptyValidation("Department", department.getSelectionModel().getSelectedItem() == null)) {

            if (userId.getText() == null || userId.getText() == "") {

                if (validation.emptyValidation("Password", password.getText().isEmpty())
                        && validation.validate("Username", username.getText(), "[a-zA-Z0-9]+")) {

                    if (!userService.ifExists(username.getText())) {

                        Teacher teacher = new Teacher();
                        teacher.setFirstName(firstName.getText());
                        User user = new User();
                        user.setRole("teacher");
                        user.setUsername(username.getText());
                        user.setPassword(password.getText());
                        userService.persist(user);

                        teacher.setUser(user);
                        teacher.setLastName(lastName.getText());
                        teacher.setDegree(degree.getValue());
                        teacher.setDepartment(department.getValue());

                        teacherService.persist(teacher);

                        reset();
                        updateTable();
                        saveAlert(teacher);

                    } else {
                        usernameAlreadyExists();
                    }

                }

            } else {

                Teacher teacher = teacherService.find(Integer.parseInt(userId.getText()));

                teacher.setFirstName(firstName.getText());
                teacher.setLastName(lastName.getText());
                teacher.setDegree(degree.getValue());
                teacher.setDepartment(department.getValue());

                teacherService.update(teacher);

                reset();
                updateTable();

                updateAlert(teacher);

            }

        }

    }

    private void usernameAlreadyExists() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("User with this username already exists. Please use a different username");
    }

    private void deleteAlert(Teacher teacher) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful delete");
        alert.setHeaderText(null);
        alert.setContentText("The student with the id  " + teacher.getId() +" was deleted successfully");
        alert.showAndWait();
    }

    private void saveAlert(Teacher teacher){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful save");
        alert.setHeaderText(null);
        alert.setContentText("The student with the id  "+ teacher.getId() +" has been created.");
        alert.showAndWait();
    }

    private void updateAlert(Teacher teacher){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful update");
        alert.setHeaderText(null);
        alert.setContentText("The student with the id  "+ teacher.getId() +" has been updated.");
        alert.showAndWait();
    }


    private void loadUserDetails() {
        teachersList.clear();
        teachersList.addAll(teacherService.findAll());
        userTable.setItems(teachersList);
    }

    private void setColumnProperties(){
        colUserId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colUsername.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUser().getUsername()));
        colFirstName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName()));
        colLastName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastName()));
        colDegree.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDegree()));
        colDepartment.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment()));

    }

    public void fillTextFields() {
        Teacher teacher = userTable.getSelectionModel().getSelectedItem();
        password.setDisable(true);
        userId.setText(teacher.getId().toString());
        username.setText(teacher.getUser().getUsername());
        username.setDisable(true);
        firstName.setText(teacher.getFirstName());
        lastName.setText(teacher.getLastName());
        degree.setValue(teacher.getDegree());
        department.setValue(teacher.getDepartment());
        password.setDisable(true);
    }

    public void updateTable() {
        loadUserDetails();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        degree.setItems(degrees);
        department.setItems(departments);
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
