package il.cshaifasweng.OCSFMediatorExample.client;

        import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
        import il.cshaifasweng.OCSFMediatorExample.entities.Principal;
        import il.cshaifasweng.OCSFMediatorExample.entities.Student;
        import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;
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

public class PrincipleExamsController extends BaseController {
List<Exam>exams;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane exam_content;

    @FXML
    private TextField Explanation_TF;

    @FXML
    private ChoiceBox<String> choosen_exam_choice_box;

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
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        try {
            SimpleClient.getClient().sendToServer("AllExams");
            choosen_exam_choice_box.setOnAction(this::ExamDetected);

        }       catch (IOException e){
        e.printStackTrace();
    }
    }
    public void ShowExams()
    {
        List<String> examIds = new ArrayList<>();

        for (Exam exam : exams) {
            examIds.add(exam.getId()+" "+exam.getName());

        }
        Platform.runLater(() -> {

            choosen_exam_choice_box.setItems((FXCollections.observableArrayList(examIds)));    });

    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        if (msg instanceof List<?>) {
            System.out.println("itsss exmasppp list");
            List<?> list = (List<?>) msg;
            if (!list.isEmpty()) {
                System.out.println(list.size());

                List<Exam>allex=new ArrayList<>();
                Object firstElement = list.get(0);
                if (firstElement instanceof Exam) {
                    for(Object obj:list) {
                        allex.add((Exam) obj);
                    }
                    exams=allex;
                    System.out.println(exams.size());
                    ShowExams();
                }
            }
        }
        else if(msg instanceof Exam)
        {
            System.out.println("its exaaampp");
            TextArea examTextArea = new TextArea();
            examTextArea.setText(((Exam) msg).generateExamText());
            Platform.runLater(() -> {

                    exam_content.setContent(examTextArea);


            });

        }
    }

    @FXML
    void ExamDetected(ActionEvent event)  {
        try {
            SimpleClient.getClient().sendToServer("Exam" + choosen_exam_choice_box.getSelectionModel().getSelectedItem().split(" ")[0]);
        }catch (IOException e){
            e.printStackTrace();
        }
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

}
