package pjv.controller.admin;

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
import pjv.model.Teacher;
import pjv.model.User;
import pjv.service.TeacherService;
import pjv.service.UserService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AdminTeachersController implements Initializable {

    @FXML
    private MenuItem homeMenuItem;

    @FXML
    private MenuItem studentsMenuItem;

    @FXML
    private MenuItem subjectsMenuItem;

    @FXML
    private Label userId;

    @FXML
    private TextField username;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private ComboBox<String> degree;

    @FXML
    private ComboBox<String> department;

    @FXML
    private PasswordField password;

    @FXML
    private Button reset;

    @FXML
    private Button saveUser;

    @FXML
    private Button btnLogout;

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

    @FXML
    private MenuItem deleteUsers;

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
    void toHome(ActionEvent event) {

    }

    @FXML
    void toStudents(ActionEvent event) {

    }

    @FXML
    void toSubjects(ActionEvent event) {

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
    }

    @FXML
    void save(ActionEvent event) {

        if (validation.validate("First Name", firstName.getText(), "[a-zA-Z]+") &&
                validation.validate("Last Name", lastName.getText(), "[a-zA-Z]+") &&
                validation.validate("Username", username.getText(), "[a-zA-Z0-9][a-zA-Z0-9._]") &&
                validation.emptyValidation("Degree", degree.getSelectionModel().getSelectedItem() == null) &&
                validation.emptyValidation("Department", department.getSelectionModel().getSelectedItem() == null)) {

            if (userId.getText() == null || userId.getText() == "") {
//                if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
//                        emptyValidation("Password", getPassword().isEmpty())){

//                if (validation.emptyValidation("Password", password.getText().isEmpty())) {
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

                reset(); //TODO: pridat alert
                updateTable();


            } else {

                Teacher teacher = teacherService.find(Integer.parseInt(userId.getText()));

                teacher.setFirstName(firstName.getText());
                teacher.setLastName(lastName.getText());
                teacher.setDegree(degree.getValue());
                teacher.setDepartment(department.getValue());

                teacherService.update(teacher);

                reset(); //TODO: pridat alert
                updateTable();

            }

        }

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
