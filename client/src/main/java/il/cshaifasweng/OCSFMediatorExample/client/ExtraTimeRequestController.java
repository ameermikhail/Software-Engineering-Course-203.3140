package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.PlannedExam;
import il.cshaifasweng.OCSFMediatorExample.entities.Time_Extention_request;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

public class ExtraTimeRequestController extends BaseController {
    PlannedExam currPlanned;
    @FXML
    private TextField Execution_Code;

    @FXML
    private TextField Extra_Time;

    @FXML
    private Button Send_Request;

    @FXML
    private AnchorPane rootPane;
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
    }

    public int checkFeilds() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong/Missing Feilds");
        alert.setHeaderText("Please correct the follwoing issues:");

        if (this.app.checkIfTextIsNumber(Extra_Time.getText()) == 0) {
            alert.setContentText("please enter a Time Addition");
            alert.showAndWait();
            return 0;
        }
        if (this.app.checkIfTextIsNumber(Execution_Code.getText()) == 0 || Execution_Code.getText().length() != 4) {
            alert.setContentText("please enter a valid 4 digits number exam execution code");
            alert.showAndWait();
            return 0;
        }
        System.out.println("asd"+String.valueOf(this.app.getTeacher().getPlannedExams().size()));
         currPlanned = getPlannedExamById(this.app.getTeacher().getPlannedExams(),Integer.parseInt(Execution_Code.getText()));
        if (currPlanned == null) {
            alert.setContentText("Exam code doesnt exist! pick a valid one");
            alert.showAndWait();
            return 0;
        }
return 1;

    }
    public static PlannedExam getPlannedExamById(List<PlannedExam> plannedExams, int id) {
        for (PlannedExam plannedExam : plannedExams) {
            if (plannedExam.getId() == id) {
                return plannedExam;
            }
        }
        return null; // Return null if no matching PlannedExam is found
    }


    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }
    @FXML
    void SendRequest(ActionEvent event) {
        if (checkFeilds() == 1) {
             //currPlanned = this.app.getTeacher().GetPlannedExamWithId(Integer.parseInt(Execution_Code.getText()));
            Time_Extention_request NewTimeRequest = new Time_Extention_request(Integer.parseInt(Extra_Time.getText()), currPlanned, this.getApp().teacher);
            NewTimeRequest.setIsdone(0);
            try{
                SimpleClient.getClient().sendToServer(NewTimeRequest);
            }catch (IOException e){
                e.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess!");
            alert.setHeaderText("Sucess notification!");
            alert.setContentText("Time Request Sent Succesfully");
            alert.showAndWait();
            Platform.runLater(() -> {
                try {
                    app.setWindowTitle("Teacher Menu ");
                    app.setContent("teacher_menu");
                } catch (IOException e){e.printStackTrace();
                }

            });
        }


    }
}
