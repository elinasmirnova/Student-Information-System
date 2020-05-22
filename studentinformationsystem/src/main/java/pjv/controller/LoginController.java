package pjv.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.service.UserService;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the first scene, where user should enter his credentials and log in
 */
@Controller
public class LoginController implements Initializable {

    @FXML
    private JFXTextField username;

    @FXML
    private JFXPasswordField password;

    @FXML
    private Button btnLogin;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private UserService service;

    //private Authorization authorization;

    public static String authorizationLogin;

    /**
     * Initializing scene, checks entered credentials
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(event -> {
                    if (service.authenticate(username.getText(), password.getText())) {
                        authorizationLogin = username.getText();
                        String role = service.checkRole(username.getText());
                        if (role.equals("admin")) {
                            stageManager.switchScene(FxmlView.ADMIN_MAIN);
                        } else if (role.equals("teacher")) {
                            stageManager.switchScene(FxmlView.TEACHER_MAIN);
                        } else if (role.equals("student")) {
                            stageManager.switchScene(FxmlView.STUDENT_MAIN);

                        }
                    } else {
                        validationAlert();
                        username.requestFocus();
                        password.requestFocus();
                    }
                }
        );

    }

    private void validationAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText("Bad credentials");
        alert.showAndWait();
    }
}
