package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.Subscribe;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class controller3 {


    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField Question_feild;

    @FXML
    private PasswordField answer1;

    @FXML
    private Button add_question_button;

    @FXML
    private PasswordField answer2;

    @FXML
    private PasswordField answer3;

    @FXML
    private PasswordField answer4;

    @FXML
    private ChoiceBox<String> right_answer_drop;

    @FXML
    private ChoiceBox<String> subject_drop;

    @FXML
    private ChoiceBox<String> Qnumber_drop;



    @FXML
    private PasswordField question_id;
    public void initialize() {
        // Populate the choice box with choices
        right_answer_drop.getItems().addAll("1", "2", "3","4");

        // Set a default choice
        right_answer_drop.setValue("1");
    }
@Subscribe
    public void handleEvent(Object msg) throws IOException {
    }

    @FXML
    void add_clicked(MouseEvent event) throws IOException {
        String subject =subject_drop.getSelectionModel().getSelectedItem();
        String question_code=Qnumber_drop.getSelectionModel().getSelectedItem();
        String question = subject+question_code;
        String answer1Text = answer1.getText();
        String answer2Text = answer2.getText();
        String answer3Text = answer3.getText();
        String answer4Text = answer4.getText();
        String rightAnswer = right_answer_drop.getValue();
        switch (right_answer_drop.getValue()) {
            case "1":
                rightAnswer = answer1Text;
                break;
            case "2":
                rightAnswer = answer2Text;
                break;
            case "3":
                rightAnswer = answer3Text;
                break;
            case "4":
                rightAnswer = answer4Text;
                break;
            default:
                rightAnswer = ""; // Handle the case when no option is selected
        }
        String questionId = question_id.getText();

       // Question questionObject = new Question(Integer.parseInt(subject),Integer.parseInt(question_code), answer1Text, answer2Text, answer3Text, answer4Text, rightAnswer);

       // SimpleClient.getClient().sendToServer(questionObject);


    }

}
