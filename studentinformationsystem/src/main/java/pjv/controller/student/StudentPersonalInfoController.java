package pjv.controller.student;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentPersonalInfoController implements Initializable {

    @FXML
    private HBox classesButton;

    @FXML
    private HBox assignmentsButton;

    @FXML
    private HBox studyResultsButton;

    @FXML
    private HBox examsButton;

    @FXML
    private HBox personalInfoButton;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstNameTextField.setText("Kelmel");
    }
}
