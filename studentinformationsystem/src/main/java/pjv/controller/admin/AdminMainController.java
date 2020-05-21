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
import java.util.logging.Logger;

/**
 * Controller for the first main scene when user is logged on as an administrator
 */
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

    Logger LOGGER = Logger.getLogger(AdminMainController.class.getName());

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Admin was logged out");
    }

    /**
     * Initializing scene
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectsButton.setOnAction(event -> stageManager.switchScene(FxmlView.ADMIN_SUBJECTS));

        studentsButton.setOnAction(event -> stageManager.switchScene(FxmlView.ADMIN_STUDENTS));

        teachersButton.setOnAction(event -> stageManager.switchScene(FxmlView.ADMIN_TEACHERS));

    }
}
