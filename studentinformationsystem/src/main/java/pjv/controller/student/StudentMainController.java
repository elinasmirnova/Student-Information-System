package pjv.controller.student;

import javafx.event.ActionEvent;
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
 * Controller for the main scene for the student
 */
@Controller
public class StudentMainController implements Initializable {

    @FXML
    private Button subjectsButton;

    @FXML
    private Button examsButton;

    @FXML
    private Button assignmentsButton;

    @FXML
    private Button studyResultsButton;

    @FXML
    private Button personalInfoButton;

    @Lazy
    @Autowired
    private StageManager stageManager;

    Logger LOGGER = Logger.getLogger(StudentMainController.class.getName());

    @FXML
    void logout(ActionEvent event) {
        stageManager.switchScene(FxmlView.LOGIN);
        LOGGER.info("Student was logged out");
    }

    /**
     * Initializing scene, sets actions on clicking the redirecting buttons.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        subjectsButton.setOnAction(event -> stageManager.switchScene(FxmlView.STUDENT_SUBJECTS));

        examsButton.setOnAction(event -> stageManager.switchScene(FxmlView.STUDENT_EXAMS));

        assignmentsButton.setOnAction(event -> stageManager.switchScene(FxmlView.STUDENT_ASSIGNMENTS));

        studyResultsButton.setOnAction(event -> stageManager.switchScene(FxmlView.STUDENT_STUDY_RESULTS));

        personalInfoButton.setOnAction(event -> stageManager.switchScene(FxmlView.STUDENT_PERSONAL_INFO));

    }
}
