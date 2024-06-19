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
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditQuestionController extends BaseController{
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField Question_feild;

    @FXML
    private Button add_question_bt;

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
    private ChoiceBox<String> QuestionChooseScroll;

    @FXML
    private ChoiceBox<String> SubjectScroll;

    @FXML
    private TextField RightAnswer;
    Subject subject;
    List<Question>questions;
    public void ShowSubjects()
    {
        List<String> subjects_names = this.app.getTeacher().GetSubjectsNames();
        SubjectScroll.setItems(FXCollections.observableArrayList(subjects_names));
        QuestionChooseScroll.setOnAction(this::ChooseQuestionDone);


    }


    @FXML
    void SubjectDetected(ActionEvent event) {
        this.subject=this.app.getTeacher().getsubjectWithNumber(Integer.parseInt(SubjectScroll.getValue().split(" ")[0]));
        System.out.println("subject id"+String.valueOf(subject.getid()));
        List<String>QuestionsNames=subject.getQuestionIds();
        System.out.println(QuestionsNames.size());
        this.questions=subject.getQuestions();
        System.out.println(questions.size());

        QuestionChooseScroll.setItems(FXCollections.observableArrayList(QuestionsNames));

    }
    @FXML
    void ChooseQuestionDone(ActionEvent event) {
        Question curr_Question=this.subject.GetQuestionWithId(QuestionChooseScroll.getValue());
        Question_feild.setText(curr_Question.getQuestionText());
        Answer_1.setText(curr_Question.getAnswers().get(0));
        Answer_2.setText(curr_Question.getAnswers().get(1));
        Answer_3.setText(curr_Question.getAnswers().get(2));
        Answer_4.setText(curr_Question.getAnswers().get(3));
        RightAnswer.setText(String.valueOf(curr_Question.getCorrectAnswer()));

    }

    @FXML
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        ShowSubjects();
        SubjectScroll.setOnAction(this::SubjectDetected);
    }

    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }




    @Subscribe
    public void handleEvent(Object msg) throws IOException {
    }

    @FXML
    void add_clicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong/Missing Feilds");
        alert.setHeaderText("Please correct the follwoing issues:");

        if (SubjectScroll.getValue() == null) {
            alert.setContentText("Please select a subject");
            alert.showAndWait();
            return ;
        }
        if (QuestionChooseScroll.getValue() == null) {
            alert.setContentText("Please select a question");
            alert.showAndWait();
            return ;
        }
        if (Question_ID.getText() .equals("")) {
            alert.setContentText("Please enter new question id");
            alert.showAndWait();
            return ;
        }
        if (Question_ID.getText().length()!=4||!this.app.isNumeric(Question_ID.getText())) {
            alert.setContentText("Please enter A valid 4 digits(numbers) new question id");
            alert.showAndWait();
            return ;
        }
        if (RightAnswer.getText().equals("")) {
            alert.setContentText("Please type the right answer");
            alert.showAndWait();
            return ;
        }
        if(Integer.parseInt(RightAnswer.getText())<1||Integer.parseInt(RightAnswer.getText())>4)
        {
            alert.setContentText("Please type a valid right answer 1-4");
            alert.showAndWait();
            return ;
        }



        String questionText = Question_feild.getText();
        String answer1 = Answer_1.getText();
        String answer2 = Answer_2.getText();
        String answer3 = Answer_3.getText();
        String answer4 = Answer_4.getText();
        int questionID = Integer.parseInt(Question_ID.getText());
        String rightAnswer = RightAnswer.getText();
        // Create a list to store the answers in order
        List<String> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);

        // Convert rightAnswer to an integer
        int correctAnswer = Integer.parseInt(rightAnswer);
//check id stuff

       // Subject subject = this.app.getTeacher().getsubjectWithNumber(Integer.parseInt(SubjectScroll.getValue()));
                Question newQuestion = new Question(subject, questionID,questionText, answers, correctAnswer);
                try
                {
                SimpleClient.getClient().sendToServer(newQuestion);
                }catch (IOException e) {
                    e.printStackTrace();
                }
        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
        alert2.setTitle("Sucess!");
        alert2.setHeaderText("Sucess notification!");
        alert2.setContentText("Question Added Succesfully");
        alert2.showAndWait();
        Platform.runLater(() -> {
            try {
                app.setWindowTitle("Teacher Menu ");
                app.setContent("teacher_menu");
            } catch (IOException e){e.printStackTrace();
            }

        });

//send to server


    }




}
