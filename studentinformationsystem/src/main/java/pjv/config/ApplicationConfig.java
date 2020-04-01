package pjv.config;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.ResourceBundle;

@Configuration
public class ApplicationConfig {

    @Autowired
    SpringFXMLLoader springFXMLLoader;

//    @Bean
//    @Scope("prototype")
//    public ExceptionWriter exceptionWriter() {
//        return new ExceptionWriter(new StringWriter());
//    }

    @Bean
    public ResourceBundle resourceBundle() {
        return ResourceBundle.getBundle("Bundle");
    }

    @Bean
    @Lazy(value = true)
    public StageManager stageManager(Stage stage) throws IOException {
        return new StageManager(springFXMLLoader, stage);
    }
}
