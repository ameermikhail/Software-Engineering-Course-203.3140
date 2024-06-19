package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineExamController extends BaseController{
    GradedExam graded;
        Exam exam;
        List<Question> questions;
        int[] answers;
        int index=0;
        int size;
    private int TIMER_DURATION = 3 * 60 * 60; // for example 3 hours in seconds
    private int remainingSeconds;
        @FXML
        private AnchorPane rootPane;

        @FXML
        private Button StartButton;

       @FXML
        private TextArea QuestionText;

        @FXML
        private Label QuestionNumberLabel;

        @FXML
        private Button previousButton;

        @FXML
        private Button NextButton;

        @FXML
        private ChoiceBox<Integer> AnswersChoiceBox;

        @FXML
        private Label AnswersLabel;
    @FXML
    private Label TimeLeftLabel;

    public void initialize() throws IOException {


        EventBus.getDefault().register(this);
AnswersChoiceBox.setOnAction(this::AnswerPicked);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong/Missing Feilds");
        alert.setHeaderText("Please correct the follwoing issues:");

        Student student=this.app.getStudent();
        PlannedExam plannedExam=this.app.getPlannedExam();
        size=plannedExam.getExam().getQuestions().size();
        System.out.println(String.valueOf(size)+"qwwwwww");
        if(plannedExam==null)
        {
            alert.setContentText("You don't have access to that exam");
            alert.showAndWait();
            return;
        }
        if(plannedExam!=null&&plannedExam.getFinished()==1)
        {
            alert.setContentText("Exam Is finished!");
            alert.showAndWait();
            return;
        }
        graded=new GradedExam(this.app.getStudent(),plannedExam.getTeacher(),plannedExam);
        exam=plannedExam.getExam();
        this.questions=exam.getQuestions();
        answers=new int[exam.getQuestions().size()];
        Arrays.fill(answers,-1);
        index=0;
        ShowQustion();
        LocalDateTime startTime=plannedExam.getStartTime();
        LocalDateTime now = LocalDateTime.now();

        Duration duration = Duration.between(startTime, now);
        long secondsDiff = duration.getSeconds();

        remainingSeconds = exam.getExamDuration()*60-( int) secondsDiff;


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

    @FXML
    private Label TimeLeftValue;
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
                }
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

    private void updateTimerLabel() {
        int hours = remainingSeconds / 3600;
        int minutes = (remainingSeconds % 3600) / 60;
        int seconds = remainingSeconds % 60;

        String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        Platform.runLater(() -> TimeLeftLabel.setText(timeString));
    }

    public void addTime(int additionalSeconds) {
        remainingSeconds += additionalSeconds;
        updateTimerLabel();
    }


    @FXML
        void AnswerPicked(ActionEvent event) {
          if(AnswersChoiceBox.getSelectionModel().getSelectedItem()==null)return;
           answers[index]= AnswersChoiceBox.getSelectionModel().getSelectedItem();
          System.out.println(String.valueOf(index)+"::"+String.valueOf(AnswersChoiceBox.getSelectionModel().getSelectedItem()));
          System.out.println("right:"+questions.get(index).getCorrectAnswer());

        }

        @FXML
        void NextButton(ActionEvent event) {
            if(index==size-1)return;

                index++;
ShowQustion();

        }

        @FXML
        void PreviousButton(ActionEvent event) {
        if(index==0)return;
        index--;
ShowQustion();
        }


        @FXML
        void StartExam(ActionEvent event) {


        }
    @FXML
    void SubmitClicked(ActionEvent event) {
        try {
            List<Integer> list = new ArrayList<>();
            for (int num : answers) {
                list.add(num);
            }
            List<String>QuestionAnswers=this.app.getQuestionIds(questions);
            for (int j = 0; j < QuestionAnswers.size(); j++) {
                String answer = QuestionAnswers.get(j);
                System.out.println("Answer " + j + ": " + answer);
            }

System.out.println(list);
            graded.setStudentanswers(list);
            graded.gradeExam();
            graded.setIsFinished(1);
            this.app.plannedExam.addGradedExam(graded);
            SimpleClient.getClient().sendToServer(graded);
        } catch (IOException e) {
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
        Platform.runLater(this::Gohome);

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
        });
            try {
                List<Integer> list = new ArrayList<>();
                for (int num : answers) {
                    list.add(num);
                }

                graded.setStudentanswers(list);
                graded.gradeExam();
                graded.setIsFinished(1);
                graded.getPlannedExam().setFinished(1);

                SimpleClient.getClient().sendToServer(graded);
                this.app.plannedExam.addGradedExam(graded);
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                Platform.runLater(() -> {

                    alert2.setTitle("Time is up!");
                alert2.setHeaderText("Time is up!");
                alert2.setContentText("Time is up ! your exam has been submitted");
                alert2.showAndWait();
                Gohome(); });
            } catch (IOException e) {
                e.printStackTrace();
            }


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




        void ShowQustion()
        {

            QuestionNumberLabel.setText("Question "+Integer.toString(index+1));
            QuestionText.setText(questions.get(index).QuestionToText());
            AnswersChoiceBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4));
            if(answers[index]!=-1)
            {
                AnswersChoiceBox.setValue(answers[index]);
            }


        }

    }
