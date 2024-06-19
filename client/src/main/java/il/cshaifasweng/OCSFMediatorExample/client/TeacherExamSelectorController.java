package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class TeacherExamSelectorController extends BaseController{

    @FXML
    private ListView<?> Choose_Exam;

    @FXML
    private Button Main_Page;

    @FXML
    private Button View_Grades_Sheet;

    @FXML
    private AnchorPane rootPane;

    @FXML
    void MainPage(ActionEvent event) {

    }

    @FXML
    void ViewGradesSheet(ActionEvent event) {

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
