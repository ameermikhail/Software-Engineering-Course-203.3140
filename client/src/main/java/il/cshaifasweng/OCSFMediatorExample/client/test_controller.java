
package il.cshaifasweng.OCSFMediatorExample.client;

        import java.net.URL;
        import java.util.ResourceBundle;
        import javafx.fxml.FXML;
        import javafx.scene.control.TextField;

public class test_controller extends BaseController  {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField fff;

    @FXML
    void initialize() {
       fff.setText(String.valueOf(app.getId()));

    }
    public test_controller(App app) {
        setApp(app);
    }
}
