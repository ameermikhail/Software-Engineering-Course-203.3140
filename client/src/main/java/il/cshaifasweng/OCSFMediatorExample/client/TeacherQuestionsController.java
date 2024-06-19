
package il.cshaifasweng.OCSFMediatorExample.client;

        import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
        import il.cshaifasweng.OCSFMediatorExample.entities.Question;
        import javafx.collections.FXCollections;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.AnchorPane;
        import org.greenrobot.eventbus.EventBus;
        import org.greenrobot.eventbus.Subscribe;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;

public class TeacherQuestionsController extends BaseController {
    List<Question> Questions;
    Question curr;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane QuestionContent;

    @FXML
    private TextField Explanation_TF;

    @FXML
    private ChoiceBox<String> chooseQuestion;

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
    void QuestionDetected(ActionEvent event) {
        if(chooseQuestion.getSelectionModel().getSelectedItem()==null)return;

        for (Question question : Questions) {
            //System.out.println(question.getQuestionId());
            //System.out.println("weeeee");
            //System.out.println(chooseQuestion.getSelectionModel().getSelectedItem());

            if (question.getQuestionId() == Integer.parseInt(chooseQuestion.getSelectionModel().getSelectedItem().split(" ")[0])) {
                System.out.println("maadeit");
                curr=question;
            }


        }
        TextArea examTextArea = new TextArea();
        examTextArea.setText(curr.QuestionToText2());
        QuestionContent.setContent(examTextArea);



    }

    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }

    @FXML
    void HomePageClicked(ActionEvent event) {

    }
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        chooseQuestion.setOnAction(this::QuestionDetected);
        Questions=this.app.getTeacher().getQuestionsInsubjects();

        chooseQuestion.setItems(FXCollections.observableArrayList(this.app.getTeacher().getQuestionsinSubjectsIDDS()));

        System.out.println(Questions.size());
    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {

        if(msg instanceof Question)
        {

        }
                }
            }





