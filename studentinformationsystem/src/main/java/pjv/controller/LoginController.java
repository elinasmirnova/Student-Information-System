package pjv.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.service.UserService;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoginController implements Initializable {

    @FXML
    private Label lblLogin;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button btnLogin;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private UserService service;

    //private Authorization authorization;

    public static String authorizationLogin;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
      btnLogin.setOnAction(event -> {
          if (service.authenticate(username.getText(), password.getText())) {
              authorizationLogin = username.getText();
              switch (service.checkRole(username.getText())) {
                  case "admin":
                      stageManager.switchScene(FxmlView.ADMIN_MAIN);
                      break;
                  case "teacher":
                      stageManager.switchScene(FxmlView.TEACHER_MAIN);
                      break;
                  case "student":
                      stageManager.switchScene(FxmlView.STUDENT_MAIN);
                      break;
              }

          }

//          if (userService.authenticate(username.getText(), password.getText())) {
//              stageManager.switchScene(FxmlView.ADMIN_MAIN);
//          } else if (studentService.authenticate(username.getText(), password.getText())) {
//              stageManager.switchScene(FxmlView.STUDENT_MAIN);
//          }
      });
    }

    public String getAuthorizationLogin() {
        return authorizationLogin;
    }

    public void setAuthorizationLogin(String authorizationLogin) {
        this.authorizationLogin = authorizationLogin;
    }
}
