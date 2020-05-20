package pjv.controller.student;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.LoginController;
import pjv.model.Student;
import pjv.service.StudentService;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class StudentPersonalInfoController implements Initializable {

    @FXML
    private Label labelID;

    @FXML
    private JFXTextField username;

    @FXML
    private JFXTextField firstName;

    @FXML
    private JFXTextField lastName;

    @FXML
    private JFXTextField dateOfBirth;

    @FXML
    private JFXTextField year;

    @FXML
    private JFXTextField studyProgram;

    @FXML
    private JFXTextField obtainedCredits;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField phoneNumber;

    @FXML
    private JFXButton editButton;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private StudentService studentService;

    Student student;

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
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
    void toStudyResults(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_STUDY_RESULTS);
    }

    @FXML
    void toSubjects(ActionEvent event) {
        stageManager.switchScene(FxmlView.STUDENT_SUBJECTS);
    }

    private void loadInformation() {
        labelID.setText(student.getId().toString());
        username.setText(student.getUser().getUsername());
        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());
        dateOfBirth.setText(student.getDateOfBirth().toString());
        year.setText(student.getYear().toString());
        studyProgram.setText(student.getStudyProgram());
        obtainedCredits.setText(student.getObtainedCredits().toString());
        loadPhoneAddress();
    }

    private void loadPhoneAddress() {
        address.setDisable(true);
        phoneNumber.setDisable(true);
        address.setText(student.getAddress());
        phoneNumber.setText(student.getPhoneNumber());
    }

    private void editPhoneAddress() {
        address.setDisable(false);
        phoneNumber.setDisable(false);
    }

    private void updatePhoneAddress() {
        student.setAddress(address.getText());
        student.setPhoneNumber(phoneNumber.getText());
        studentService.update(student);
        loadPhoneAddress();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        student = studentService.findByUsername(LoginController.authorizationLogin);
        loadInformation();
        editButton.setOnAction(event -> {
           if (editButton.getText().equals("Edit")) {
                editPhoneAddress();
                editButton.setText("Save");
            } else {
               updatePhoneAddress();
               editButton.setText("Edit");
            }

        });
    }
}
