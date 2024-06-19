package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.PlannedExam;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DoExamController extends  BaseController {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField ExamCodeTextField;

    @FXML
    private TextField IdTextFeild;

    Exam exam;
    @FXML
    public void initialize() {
        EventBus.getDefault().register(this);
    }
    @FXML
    void StartPressed(ActionEvent event) {
        String StudentId = IdTextFeild.getText();
        String examId = ExamCodeTextField.getText();
        if (StudentId.isEmpty() || examId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("please fill the feilds");
            alert.showAndWait();
            return;


        }
        int stid=Integer.parseInt(StudentId);


        if (this.app.getStudent().getId() != stid)
        {
            System.out.println(String.valueOf(this.app.getStudent().getId())+"XX"+StudentId);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("please enter you valid ID");
            alert.showAndWait();
            return;


        }

        try {
            SimpleClient.getClient().sendToServer("Planned:" + examId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handleEvent(Object msg) {

            if (msg instanceof PlannedExam) {
                System.out.println("planned reached doexam");
                PlannedExam planned = (PlannedExam) msg;
                if (!this.app.getCourseIdsOnly(this.app.getStudent().getCourses()).contains(String.valueOf(planned.getExam().getCourse().getCourse_id()))) {
                    Platform.runLater(() -> {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Wrong/Missing Feilds");
                    alert.setHeaderText("Please correct the follwoing issues:");
                    alert.setContentText("you are not eligable to do this exam");
                    alert.showAndWait();
                    });
                    return;
                }

                LocalDateTime currentDateTime = LocalDateTime.now();
                int comparisonResult = planned.getStartTime().compareTo(currentDateTime);

                if (comparisonResult < 0) {
                    int comparisonResult2 = planned.getStartTime().plusMinutes(planned.getExam().getExamDuration()).compareTo(currentDateTime);
                    if (comparisonResult2 > 0) {
                        // The exam has already started, but it's not finished yet
                        System.out.println("Exam is ongoing.");

                    } else {
                        // The exam has finished
                        Platform.runLater(() -> {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Exam is finished");
                        alert.setHeaderText("Exam is finished");
                        alert.setContentText("Exam is finished");
                        alert.showAndWait();
                        });
                        return;
                    }
                } else if (comparisonResult > 0) {
                    // The exam hasn't started yet
                    Platform.runLater(() -> {

                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exam hasn't started yet");
                    alert.setHeaderText("Exam hasn't started yet");
                    alert.setContentText("Exam hasn't started yet");
                    alert.showAndWait();
                    });

                    return;
                }




                System.out.println("cleaaaaar");
                this.app.plannedExam = planned;
                if (planned.getExamType().startsWith("M")) {
                    Platform.runLater(() -> {
                        try {
                            app.setWindowTitle("Manual Exam ");
                            app.setContent("ManualExam");
                        } catch (IOException e){e.printStackTrace();
                    }

                    });
                }
                if (planned.getExamType().startsWith("O")) {

                    Platform.runLater(() -> {
                        try {
                            app.setWindowTitle("Online Exam ");
                            app.setContent("onlineExamExecution");
                        } catch (IOException e){e.printStackTrace();
                        }

                    });
                }
            }
            if(msg instanceof String)
            {

                if(((String) msg).startsWith("plannedwrong"))
                {
                    Platform.runLater(() -> {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Exam Execution Code");
                    alert.setContentText("Please type a valid exam execution code");
                    alert.showAndWait();
                    });


                }
            }



    }
    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }

}
