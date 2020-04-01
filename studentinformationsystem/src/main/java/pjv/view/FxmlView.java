package pjv.view;

import java.util.ResourceBundle;

public enum FxmlView {

    MAIN {
        public String getTitle() {
            return getStringFromResourceBundle("main.title");
        }

        public String getFxmlFile() {
            return "/fxml/login.fxml";
        }

    },
    LOGIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("login.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/login.fxml";
        }
    };

    public abstract String getTitle();
    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
