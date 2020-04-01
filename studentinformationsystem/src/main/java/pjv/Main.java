package pjv;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pjv.config.StageManager;
import pjv.view.FxmlView;


@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext context;
    private StageManager stageManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        context = springBootApplicationContext();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stageManager = context.getBean(StageManager.class, stage);
        displayInitialScene();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    private void displayInitialScene() {
        stageManager.switchScene(FxmlView.LOGIN);
    }

    private ConfigurableApplicationContext springBootApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        String[] args = getParameters().getRaw().toArray(new String[0]);
        return builder.run(args);
    }
}
