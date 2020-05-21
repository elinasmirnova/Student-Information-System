package pjv.view;

import java.util.ResourceBundle;

/**
 * Enum which contains all the scenes and paths to them
 */
public enum FxmlView {

    ADMIN_MAIN {
        public String getTitle() {
            return getStringFromResourceBundle("admin_main.title");
        }

        public String getFxmlFile() {
            return "/fxml/admin/admin_main.fxml";
        }

    },
    ADMIN_STUDENTS {
        public String getTitle() {
            return getStringFromResourceBundle("admin_students.title");
        }

        public String getFxmlFile() {
            return "/fxml/admin/admin_students.fxml";
        }

    },
    ADMIN_TEACHERS {
        public String getTitle() {
            return getStringFromResourceBundle("admin_teachers.title");
        }

        public String getFxmlFile() {
            return "/fxml/admin/admin_teachers.fxml";
        }

    },
    ADMIN_SUBJECTS {
        public String getTitle() {
            return getStringFromResourceBundle("admin_subjects.title");
        }

        public String getFxmlFile() {
            return "/fxml/admin/admin_subjects.fxml";
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
    },
    STUDENT_MAIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("student_main.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/student/student_main.fxml";
        }
    },
    STUDENT_ASSIGNMENTS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("student_assignments.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/student/student_assignments.fxml";
        }
    },
    STUDENT_EXAMS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("student_exams.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/student/student_exams.fxml";
        }
    },
    STUDENT_PERSONAL_INFO {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("student_personalinfo.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/student/student_personalinfo.fxml";
        }
    },
    STUDENT_STUDY_RESULTS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("student_studyresults.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/student/student_studyresults.fxml";
        }
    },
    STUDENT_SUBJECTS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("student_subjects.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/student/student_subjects.fxml";
        }
    },

    TEACHER_MAIN {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teacher_main.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/teacher/teacher_main.fxml";
        }
    },
    TEACHER_SUBJECTS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teacher_subjects.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/teacher/teacher_subjects.fxml";
        }
    },
    TEACHER_EXAMS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teacher_exams.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/teacher/teacher_exams.fxml";
        }
    },
    TEACHER_ASSIGNMENTS {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("teacher_assignments.title");
        }

        @Override
        public String getFxmlFile() {
            return "/fxml/teacher/teacher_assignments.fxml";
        }
    };


    public abstract String getTitle();
    public abstract String getFxmlFile();

    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("Bundle").getString(key);
    }
}
