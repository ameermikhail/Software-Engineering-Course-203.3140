
package il.cshaifasweng.OCSFMediatorExample.client;

        import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
        import il.cshaifasweng.OCSFMediatorExample.entities.Question;
        import javafx.application.Platform;
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

public class PrincipalViewQuetions extends BaseController {
 List<Question>Questions;
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
        //System.out.println(Questions.size());
        int targetId = Integer.parseInt(chooseQuestion.getSelectionModel().getSelectedItem().split(" ")[0]);

        for (Question question : Questions) {
            if (question.getQuestionId() == targetId) {
                curr = question;
                break;  // Exit the loop once we find our match
            }
        }
            if(curr==null)return;


        TextArea examTextArea = new TextArea();
        examTextArea.setText(curr.QuestionToText2());

        QuestionContent.setContent(examTextArea);



    }

    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        System.out.println("it was clicked");
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        System.out.println(buttonId);
        String idWithoutSuffix = buttonId.replace("_button", "");
        System.out.println(idWithoutSuffix);
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }

    @FXML
    void HomePageClicked(ActionEvent event) throws IOException {
        app.setWindowTitle("principal Menu");
        app.setContent("principal_menu");
    }

    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        SimpleClient.getClient().sendToServer("AllQuestions");
        chooseQuestion.setOnAction(this::QuestionDetected);
    }
    public void ShowQuestions()
    {
        List<String> QuestionsIds = new ArrayList<>();

        for (Question question : Questions) {
            QuestionsIds.add(String.valueOf(question.getQuestionId())+" "+question.getQuestionText());
        }
        Platform.runLater(() -> {

        chooseQuestion.setItems((FXCollections.observableArrayList(QuestionsIds)));
        });
    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        if (msg instanceof List<?>) {
            List<?> list = (List<?>) msg;
            if (!list.isEmpty()) {
                Object firstElement = list.get(0);
                if (firstElement instanceof Question) {
                    List<Question>allques=new ArrayList<>();
                    for(Object obj:list) {
                        allques.add((Question) obj);
                    }
                    Questions=allques;
                    ShowQuestions();
                }
            }
        }

    }

}
