package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class ManualExamController extends BaseController{
PlannedExam plannedExam;
    private int TIMER_DURATION ; // for example 3 hours in seconds
    private int remainingSeconds;

    @FXML
    private Button Download_Exam;

    @FXML
    private TextField Insert_Code;

    @FXML
    private Button Submit_Exam;

    @FXML
    private TextField Time_Left;

    @FXML
    private Button Upload_Exam;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField IdTextFeild;
    @FXML
    private Label TimeLeftLabel;
    @FXML
    private Label TimeLeftValue;
    Exam exam;
    String ExamText;
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        this.plannedExam = this.app.getPlannedExam();
        this.ExamText=plannedExam.getExam().generateExamText();
        System.out.println(this.plannedExam.getId());
        LocalDateTime startTime=plannedExam.getStartTime();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(startTime, now);
        long secondsDiff = duration.getSeconds();

        remainingSeconds = this.plannedExam.getExam().getExamDuration()*60-( int) secondsDiff;

        updateTimerLabel();

        Thread timerThread = new Thread(() -> {
            while (remainingSeconds > 0) {
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                remainingSeconds--;
                if (remainingSeconds == 0) {

                    onTimerReachedZero(); // Call function when timer reaches zero
                }
                Platform.runLater(this::updateTimerLabel);
            }
        });

        timerThread.setDaemon(true);
        timerThread.start();

    }
    private void onTimerReachedZero() {
        // Whatever logic you want to happen when the timer reaches zero
        // For instance, you might want to display a message to the user:
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Time's Up!");
            alert.setHeaderText(null);
            alert.setContentText("Your exam time has finished!");
            alert.showAndWait();
            try {
                SimpleClient.getClient().sendToServer("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gohome();
        });

    }
    public void Gohome()
    {
        Platform.runLater(() -> {
            try {
                app.setWindowTitle("student_menu ");
                app.setContent("student_menu");
            } catch (IOException e){e.printStackTrace();
            }

        });
    }

    @FXML
    void DownloadExam(ActionEvent event) {
        System.out.println("Downlaod reached");
        TextToWord textToWord = new TextToWord();
        String inputText =plannedExam.getExam().generateExamText(); // Replace with your actual text
        File outputFile = new File("output.docx"); // Provide the desired output file name and path

        textToWord.convertToWord(inputText, outputFile);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucess!");
        alert.setHeaderText("Sucess notification!");
        alert.setContentText("Exam Download Succesfully");
        alert.showAndWait();

    }



    @FXML
    void InsertCode(ActionEvent event) {

    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        System.out.println("reached in manual");
        if(msg instanceof String message)
        {
            if(message.startsWith("addtime"))
            {
                String[] parts = message.split(":");
                int idd = Integer.parseInt(parts[2]);
                int addition = Integer.parseInt(parts[3]);
System.out.println("addto,e onmanural"+String.valueOf(idd)+":"+String.valueOf(addition));
                if((this.app.GetWindowsTitle().startsWith("Online")||this.app.GetWindowsTitle().startsWith("Manual"))&&this.app.getPlannedExam().getId()==idd)
                {
                    System.out.println("addotopm added");
                    addTime(addition*30);
                    Platform.runLater(() -> {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Time Addition!");
                    alert.setHeaderText("You got Time Addition!");
                    alert.setContentText("you got "+String.valueOf(addition)+ "mote minutes");
                    alert.showAndWait();
                    });

                }
            }
        }

    }
    @FXML
    void SubmitExam(ActionEvent event) {

    }

    @FXML
    void Time_Left(ActionEvent event) {

    }
    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Upload Exam File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.Word Documents", "*.docx", "*.doc"));
    }

    @FXML
    void UploadExam(ActionEvent event) {
        System.out.println("Upload reached");

        FileChooser fileChooser = new FileChooser();
        configureFileChooser(fileChooser);

        // Show the file dialog and get the selected file
        Window primaryStage = null; // You need to provide the reference to your primary stage
        File uploadedFile = fileChooser.showOpenDialog(this.app.getAppstage());

        if (uploadedFile != null) {
            WordToText wordToText = new WordToText();
            String examText = wordToText.convertToText(uploadedFile);
            ManualResult gg=new ManualResult(this.app.getStudent().getId(),this.plannedExam.getId(),examText);
            try{
                SimpleClient.getClient().sendToServer(gg);
            }catch (IOException e)
            {
                e.printStackTrace();
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess!");
            alert.setHeaderText("Sucess notification!");
            alert.setContentText("Exam Submitted Succesfully");
            alert.showAndWait();
            Platform.runLater(() -> {
                try {
                    app.setWindowTitle("Student Menu ");
                    app.setContent("student_menu");
                } catch (IOException e){e.printStackTrace();
                }

            });

        }

    }

    @FXML
    void add_clicked(MouseEvent event) {

    }

    @FXML
    void meow(MouseDragEvent event) {

    }
    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }

    private void updateTimerLabel() {
        int hours = remainingSeconds / 3600;
        int minutes = (remainingSeconds % 3600) / 60;
        int seconds = remainingSeconds % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        Platform.runLater(() -> TimeLeftLabel.setText(timeString));
    }

    public synchronized void addTime(int additionalSeconds) {
        remainingSeconds += additionalSeconds;
        updateTimerLabel();
    }

}
