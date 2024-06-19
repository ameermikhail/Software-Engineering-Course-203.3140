package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;

import javafx.event.ActionEvent;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class PrincipalMenuController extends BaseController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button principal_questions_button;

    @FXML
    private Button principal_grades_button;



    @Subscribe
    public void handleEvent(Object msg) throws IOException {
    }

    @FXML
    void principle_menu_handle(ActionEvent event) throws IOException {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }
}
