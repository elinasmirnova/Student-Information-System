package pjv.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Register Spring as the FXML Controller Factory. Allows Spring and JavaFx coexist together
 */
@Component
public class SpringFXMLLoader {

    private final ResourceBundle resourceBundle;
    private final ApplicationContext context;

    @Autowired
    public SpringFXMLLoader(ResourceBundle resourceBundle, ApplicationContext context) {
        this.resourceBundle = resourceBundle;
        this.context = context;
    }

    /**
     * Loads the FXML hierarchy as specified in the this method
     * @param fxmlPath
     * @return
     * @throws IOException
     */
    public Parent load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean);
        loader.setResources(resourceBundle);
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }
}
