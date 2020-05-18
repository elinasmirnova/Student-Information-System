package pjv.controller.admin;


import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import pjv.config.StageManager;
import pjv.controller.Validation;
import pjv.model.Subject;
import pjv.model.Teacher;
import pjv.service.SubjectService;
import pjv.service.TeacherService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class AdminSubjectsController implements Initializable {

    @FXML
    private Label mainLabel;

    @FXML
    private Label subjectId;

    @FXML
    private TextField code;

    @FXML
    private TextField name;

    @FXML
    private RadioButton rbSummer;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton rbWinter;

    @FXML
    private ComboBox<Integer> credits;

    @FXML
    private ComboBox<String> role;

    @FXML
    private CheckComboBox<String> teachersTest1;

//    @FXML
//    private CheckListView<String> teachersTest2;

    @FXML
    private TextArea txtAreaSynopsis;

    @FXML
    private Button reset;

    @FXML
    private Button saveSubject;

    @FXML
    private Button btnLogout;

    @FXML
    private TableView<Subject> subjectsTable;

    @FXML
    private TableColumn<Subject, String> colUserId;

    @FXML
    private TableColumn<Subject, String> colCode;

    @FXML
    private TableColumn<Subject, String> colName;

    @FXML
    private TableColumn<Subject, String> colRole;

    @FXML
    private TableColumn<Subject, Integer> colCredits;

    @FXML
    private TableColumn<Subject, String> colSemester;

    @FXML
    private MenuItem viewSubject;

    @FXML
    private MenuItem deleteSubject;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private TeacherService teacherService;

    private Validation validation;

    private ObservableList<Subject> subjectsList = FXCollections.observableArrayList();
    private ObservableList<String> teachersList = FXCollections.observableArrayList();
    private ObservableList<Integer> creditsList = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 25);
    private ObservableList<String> rolesList = FXCollections.observableArrayList("(P) Compulsory", "(V) Elective", "(PV)");


    @FXML
    void deleteSubject(ActionEvent event) {
        Subject subjectToDelete = subjectsTable.getSelectionModel().getSelectedItem();
        subjectService.remove(subjectToDelete);
        updateTable();
    }

    @FXML
    void saveSubject(ActionEvent event) {
//        if (validation.validate("Code", code.getText(), "([A-Za-z0-9]+\\.[A-Za-z0-9]+(\\r)?(\\n)?)") &&
//                validation.validate("Name", name.getText(), "[a-zA-Z]+") &&
//                validation.validate("Synopsis", txtAreaSynopsis.getText(), "([A-Za-z0-9]+\\.[A-Za-z0-9]+(\\r)?(\\n)?)") &&
//                validation.emptyValidation("Credits", credits.getSelectionModel().getSelectedItem() == null) &&
//                validation.emptyValidation("Role", role.getSelectionModel().getSelectedItem() == null) &&
//                (rbSummer.isSelected() || rbWinter.isSelected())) {

        if (subjectId.getText().equals("") || subjectId.getText() == null) {
            Subject subject = new Subject();
            subject.setCode(code.getText());
            subject.setName(name.getText());
            subject.setCredits(credits.getValue());
            subject.setRole(role.getValue());
            subject.setSemester( rbSummer.isSelected() ? "Summer" : "Winter" );
            subject.setSynopsis(txtAreaSynopsis.getText());
            List<Teacher> checkedTeachers = getCheckedTeachers();
            subject.setTeachers(checkedTeachers);
            subjectService.persist(subject);
            // setSubjectToTeachers(checkedTeachers, subject);
            updateTable();
            reset();
        } else {
            Subject subject = subjectService.find(Integer.parseInt(subjectId.getText()));
            subject.setCode(code.getText());
            subject.setName(name.getText());
            subject.setCredits(credits.getValue());
            subject.setRole(role.getValue());
            subject.setSemester( rbSummer.isSelected() ? "Summer" : "Winter" );
            subject.setSynopsis(txtAreaSynopsis.getText());
            List<Teacher> checkedTeachers = getCheckedTeachers();
            subject.setTeachers(checkedTeachers);
            subjectService.update(subject);

            updateTable();
            reset();

        }



        }
    //}


    public void setSubjectToTeachers(List<Teacher> teachers, Subject subject) {
        for (Teacher teacher : teachers) {
            teacher.addSubject(subject);
            teacherService.update(teacher);
        }

    }

    @FXML
    void viewMoreAboutSubject(ActionEvent event) {

    }

    public void getAllTeachersList() {
        StringBuilder builder;
        List<Teacher> list = teacherService.findAll();
        for (Teacher teacher : list ) {
            builder = new StringBuilder();
            builder.append("id - ")
                    .append(teacher.getId())
                    .append(", ")
                    .append(teacher.getFirstName())
                    .append(" ")
                    .append(teacher.getLastName());
            System.out.println(builder.length());
            teachersList.add(builder.toString());
        }
    }

    public void fillComboBoxTeachers() {
        getAllTeachersList();
        //teachersTest2.setItems(teachersList);
        teachersTest1.getItems().addAll(teachersList);
    }

    public List<Teacher> getCheckedTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        ObservableList<String> checkedTeachersString = teachersTest1.getCheckModel().getCheckedItems();
        for (String teacherString : checkedTeachersString) {
            Teacher teacher = teacherService.find(Character.getNumericValue(teacherString.charAt(5)));
            teachers.add(teacher);
        }
        return teachers;
    }

    public void reset() {
        subjectId.setText(null);
        mainLabel.setText("Add New Subject");
        saveSubject.setText("Save");
        code.clear();
        name.clear();
        credits.getSelectionModel().clearSelection();
        role.getSelectionModel().clearSelection();
        rbSummer.setSelected(false);
        rbWinter.setSelected(false);
        txtAreaSynopsis.clear();
        teachersTest1.getCheckModel().clearChecks();
    }

    public void loadSubjectsDetails() {
        subjectsList.clear();
        subjectsList.addAll(subjectService.findAll());
        subjectsTable.setItems(subjectsList);
    }

    public void setColumnProperties(){
        colUserId.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getId()));
        colCode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCode()));
        colName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        colCredits.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCredits()));
        colRole.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        colSemester.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSemester()));

    }

    public void updateTable() {
        loadSubjectsDetails();
    }

    public void fillTextFields() {
        mainLabel.setText("Edit subject");
        saveSubject.setText("Edit");
        Subject subject = subjectsTable.getSelectionModel().getSelectedItem();
        subjectId.setText(subject.getId().toString());
        code.setText(subject.getCode());
        name.setText(subject.getName());
        credits.setValue(subject.getCredits());
        role.setValue(subject.getRole());
        txtAreaSynopsis.setText(subject.getSynopsis());
        if (subject.getSemester().equals("Summer")) {
            rbSummer.setSelected(true);
        } else {
            rbWinter.setSelected(true);
        }
        teachersTest1.getCheckModel().clearChecks();
        List<Integer> idsTeachers = extractTeachers(subject);
        for (Integer id : idsTeachers) {
            teachersTest1.getItems()
                                    .forEach( item -> {
                                        if (Character.getNumericValue(item.charAt(5)) == id) {
                                            teachersTest1.getCheckModel().check(item);
                                        }
                                    }
                );
        }
    }

    public List<Integer> extractTeachers(Subject subject) {
        List<Integer> idsTeachers = new ArrayList<>();
        subjectService.find(subject.getId()).getTeachers().forEach( t -> idsTeachers.add(t.getId()));
       //subject.getTeachers().forEach( t -> idsTeachers.add(t.getId()));
        return idsTeachers;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        role.setItems(rolesList);
        credits.setItems(creditsList);
        setColumnProperties();
        loadSubjectsDetails();
        fillComboBoxTeachers();
        subjectsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                fillTextFields();
                //userTable.getSelectionModel().clearSelection();
            }
        });

        reset.setOnAction(event -> reset());
    }
}
