package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
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

public class ViewExamController extends BaseController{
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        choosen_exam_choice_box.setOnAction(this::ExamDetected);
        choosen_exam_choice_box.setItems(FXCollections.observableArrayList(this.app.getTeacher().getExamIdsInSubjects()));
    }
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

    @FXML
    void ExamDetected(ActionEvent event) {
        if(choosen_exam_choice_box.getSelectionModel()==null)return;
        Exam curr=this.app.getTeacher().getExamById(choosen_exam_choice_box.getSelectionModel().getSelectedItem());

        TextArea examTextArea = new TextArea();
        if(curr==null)return;
        examTextArea.setText(curr.generateExamText());


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
