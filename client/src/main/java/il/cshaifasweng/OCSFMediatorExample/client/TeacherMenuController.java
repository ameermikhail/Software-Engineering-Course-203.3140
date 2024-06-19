package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class TeacherMenuController extends BaseController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button view_questions_button;

    @FXML
    private Button add_question_button;

    @FXML
    private Button edit_question_button;

    @FXML
    private Button builed_exam_button;

    @FXML
    private Button prepare_exam_button;

    @FXML
    private Button execute_exam_button;

    @FXML
    private Button request_time_extention_button;

    @FXML
    private Button view_grades_button;

    @FXML
    private Button approve_grades_button;

    @FXML
    void TeacherButtonHandle(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        System.out.println(idWithoutSuffix);
        Platform.runLater(() -> {
            app.setWindowTitle(idWithoutSuffix);
            try {
                app.setContent(idWithoutSuffix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });





    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
    }
}


