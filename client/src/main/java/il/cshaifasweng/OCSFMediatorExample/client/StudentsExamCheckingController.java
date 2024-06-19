package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class StudentsExamCheckingController extends BaseController{

    @FXML
    private CheckBox Correct_Answer;

    @FXML
    private AnchorPane Incorrect_Answer;

    @FXML
    private Button Next_Question;

    @FXML
    private Button Previous_Questione;

    @FXML
    private ListView<?> View_Question;

    @FXML
    void CorrectAnswer(ActionEvent event) {

    }

    @FXML
    void IncorrectAnswer(MouseEvent event) {

    }

    @FXML
    void Next_Question(ActionEvent event) {

    }

    @FXML
    void Previous_Questione(ActionEvent event) {

    }

    @FXML
    void add_clicked(MouseEvent event) {

    }

    @FXML
    void meow(MouseDragEvent event) {

    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
    }
}
