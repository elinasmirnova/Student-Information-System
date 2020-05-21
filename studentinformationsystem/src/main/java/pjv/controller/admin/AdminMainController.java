package pjv.controller.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.view.FxmlView;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AdminMainController implements Initializable {

    @FXML
    private Button subjectsButton;

    @FXML
    private Button studentsButton;

    @FXML
    private Button teachersButton;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectsButton.setOnAction(event -> stageManager.switchScene(FxmlView.ADMIN_SUBJECTS));

        studentsButton.setOnAction(event -> stageManager.switchScene(FxmlView.ADMIN_STUDENTS));

        teachersButton.setOnAction(event -> stageManager.switchScene(FxmlView.ADMIN_TEACHERS));

    }
}
