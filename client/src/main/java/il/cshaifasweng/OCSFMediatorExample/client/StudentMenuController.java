package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class StudentMenuController extends BaseController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button vew_grades_button;

    @FXML
    private Button edit_question_button;
    public StudentMenuController(App app) {
        setApp(app);
    }

    @FXML
    void DoExamClicked(ActionEvent event) throws IOException {
        app.setWindowTitle("Do Exam");
        app.setContent("do_exam");

    }


    @FXML
    void view_grades_clciked(ActionEvent event) throws IOException {
        app.setWindowTitle("view_grades");
        app.setContent("view_grades");

    }


    @Subscribe
    public void handleEvent(Object msg) throws IOException {
    }
}
