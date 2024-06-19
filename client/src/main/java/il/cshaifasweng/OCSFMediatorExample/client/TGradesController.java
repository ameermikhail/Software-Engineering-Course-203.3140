package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.GradedExam;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class TGradesController extends BaseController{

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
    public void initialize()  {
        EventBus.getDefault().register(this);
        finished_exams=this.app.getFinishedGradedExams(this.app.getTeacher().getExams());
        List<String>graded_ids=this.app.getGradedExamIds(finished_exams);
        choosen_exam_choice_box.setOnAction(this::ExamDetected);
        choosen_exam_choice_box.setItems(FXCollections.observableArrayList(graded_ids));

    }

    @FXML
    void ExamDetected(ActionEvent event) {
        if(choosen_exam_choice_box.getSelectionModel().getSelectedItem()==null)return;
        String currid=choosen_exam_choice_box.getSelectionModel().getSelectedItem();
        List<GradedExam>relevant_graded=this.app.getGradedExamsByPlannedExamId(finished_exams,Integer.parseInt(currid));
        Exam curr=this.app.getTeacher().getExamById(choosen_exam_choice_box.getSelectionModel().getSelectedItem());
        TextArea examTextArea = new TextArea();
        examTextArea.setText(this.app.formatGradedExams(relevant_graded));
        exam_content.setContent(examTextArea);

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


}
