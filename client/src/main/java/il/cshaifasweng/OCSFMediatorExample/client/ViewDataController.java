package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class ViewDataController extends BaseController{

    @FXML
    private Button Previous_Page;

    @FXML
    private Button Previous_Page1;

    @FXML
    private Button Previous_Page3;

    @FXML
    private Button Previous_Page31;

    @FXML
    private Button Previous_Page4;

    @FXML
    private AnchorPane rootPane;

    @FXML
    void PreviousPage(ActionEvent event) {

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
