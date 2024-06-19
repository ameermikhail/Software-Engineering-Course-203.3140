package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.GradedExam;
import il.cshaifasweng.OCSFMediatorExample.entities.PlannedExam;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PGradesController extends BaseController {

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
    List<GradedExam> finished_exams;

    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
        System.out.println("zxccvbbb");
        try {
            SimpleClient.getClient().sendToServer("AllPlanned");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void ExamDetected(ActionEvent event) {
        if (choosen_exam_choice_box.getSelectionModel().getSelectedItem() == null) return;
        String currid = choosen_exam_choice_box.getSelectionModel().getSelectedItem().split(" ")[0];
        List<GradedExam> relevant_graded = this.app.getGradedExamsByPlannedExamId(finished_exams, Integer.parseInt(currid));
        TextArea examTextArea = new TextArea();
        examTextArea.setText(this.app.formatGradedExams(relevant_graded));
        exam_content.setContent(examTextArea);

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

    @Subscribe
    public void handleEvent(Object msg) throws IOException {
System.out.println("we gppt");
        List<PlannedExam>pld=new ArrayList<>();
        List<String>pldIDS=new ArrayList<>();
        List<GradedExam >allgrades=new ArrayList<>();

        if (msg instanceof List<?> list) {
            if (!list.isEmpty()) {
                System.out.println("list not empty");
                Object firstElement = list.get(0);
                if (firstElement instanceof PlannedExam) {
for(Object obj:list)
{
    for(GradedExam grd:((PlannedExam)obj).getGradedExams())
    {
        if(grd.getCorrected()==1)
            allgrades.add(grd);

    }
    pld.add((PlannedExam) obj);
    pldIDS.add(String.valueOf(((PlannedExam)obj).getId()+" "+((PlannedExam) obj).getName()));
}
                }
                    finished_exams = allgrades;
                    List<String> graded_ids = this.app.getGradedExamIds(finished_exams);
                choosen_exam_choice_box.setOnAction(this::ExamDetected);
                    choosen_exam_choice_box.setItems(FXCollections.observableArrayList(pldIDS));




            }
            else System.out.println("list is empty !!");
        }
    }
}