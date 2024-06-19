package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.Subject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddQuestionController extends BaseController {

    @FXML
    private TextField Answer_1;

    @FXML
    private TextField Answer_2;

    @FXML
    private TextField Answer_3;

    @FXML
    private TextField Answer_4;

    @FXML
    private TextField Question_ID;

    @FXML
    private TextField Question_feild;

    @FXML
    private Button add_question_bt;

    @FXML
    private ChoiceBox<String> right_answer_drop;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Menu HomePage;

    @FXML
    private Menu home_page_clicked;

    @FXML
    private MenuItem view_questions_button;

    @FXML
    private MenuItem add_question_button;

    @FXML
    private MenuItem edit_question_button;

    @FXML
    private MenuItem builed_exam_button;

    @FXML
    private MenuItem prepare_exam_button;

    @FXML
    private MenuItem execute_exam;

    @FXML
    private MenuItem request_time_extention;

    @FXML
    private MenuItem approve_grades_button;

    @FXML
    private MenuItem view_grades_button;
    @FXML
    private ChoiceBox<String> subject_select;
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
    }

    public void ShowSubjects()
    {
        List<String> subjects_names = this.app.getTeacher().GetSubjectsNames();
        subject_select.setItems(FXCollections.observableArrayList(subjects_names));


    }
    @FXML
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        ShowSubjects();
        right_answer_drop.setItems(FXCollections.observableArrayList("1","2","3","4"));
    }

    @FXML
    void SetQuestionID(ActionEvent event) {

    }

    @FXML
    void subject_selected(DragEvent event) {

    }

    private List<TextField> collectTextFields() {
        List<TextField> allTextFields = new ArrayList<>();

        rootPane.lookupAll(".text-field").forEach(node -> {
            if (node instanceof TextField) {
                allTextFields.add((TextField) node);
            }
        });
        return allTextFields;

    }
    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }
    public int checkFeilds() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Wrong/Missing Feilds");
        alert.setHeaderText("Please correct the follwoing issues:");
        List<TextField> textFieldList = new ArrayList<>();
        int feildsGood = 1;
        if (subject_select.getValue() == null) {
            alert.setContentText("Please select a subject");
            alert.showAndWait();
            return 0;
        }
        if (right_answer_drop.getValue() == null) {
            alert.setContentText("Please select a right answer");
            alert.showAndWait();
            return 0;
        }
        for (TextField textField : collectTextFields()) {
            if (textField.getText() == "")
                feildsGood = 0;
        }
        if (feildsGood == 0) {
            alert.setContentText("Please fill the question and all the answers");
            alert.showAndWait();
            return 0;

        }
        if(Question_ID.getText().length()!=3)
        {
            alert.setContentText("Question Id must be 3 digits number");
            alert.showAndWait();
            return 0;
        }
        if(Question_ID.getText().charAt(0)<'0'||Question_ID.getText().charAt(0)>'9'||Question_ID.getText().charAt(1)<'0'||Question_ID.getText().charAt(1)>'9')
        {
            alert.setContentText("Question Id must be 3 digits number");
            alert.showAndWait();
            return 0;
        }

        Subject subject =this.app.getTeacher().getsubjectWithNumber(Integer.parseInt(subject_select.getValue()));
        for (Question question:subject.getQuestions())
        {
            if(question.getQuestionId()==Integer.parseInt(Question_ID.getText()))
            {
                alert.setContentText("Question Id already taken , please pick another question Id");
                alert.showAndWait();
                return 0;
            }
        }
        return 1;
    }




    @FXML
    void add_clicked(ActionEvent event)  {
        try {


            if (checkFeilds() == 1)
            {
                String question = Question_feild.getText();
                String answer1 = Answer_1.getText();
                String answer2 = Answer_2.getText();
                String answer3 = Answer_3.getText();
                String answer4 = Answer_4.getText();
                String questionId = Question_ID.getText();
                String selectedSubject = subject_select.getValue();
                String rightAnswer = right_answer_drop.getValue();

                Subject currSubject = this.app.teacher.getsubjectWithNumber(Integer.parseInt(selectedSubject));
                List<String> answers = new ArrayList<>();
                answers.add(answer1);
                answers.add(answer2);
                answers.add(answer3);
                answers.add(answer4);

                Question newQuestion = new Question(currSubject, Integer.parseInt(questionId), question, answers, Integer.parseInt(rightAnswer));
                SimpleClient.getClient().sendToServer(newQuestion);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucess!");
                alert.setHeaderText("Sucess notification!");
                alert.setContentText("Question Added Succesfully");
                alert.showAndWait();
                Platform.runLater(() -> {
                    try {
                        app.setWindowTitle("Teacher Menu ");
                        app.setContent("teacher_menu");
                    } catch (IOException e){e.printStackTrace();
                    }

                });

            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
