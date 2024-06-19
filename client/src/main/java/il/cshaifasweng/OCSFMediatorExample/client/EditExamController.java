package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.Subject;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditExamController extends BaseController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button Remove_Question;

    @FXML
    private Button SaveExam;

    @FXML
    private ChoiceBox<String> RemoveQuestionScroll;

    @FXML
    private ChoiceBox<String> EditQuetionScroll;

    @FXML
    private ChoiceBox<String> AddQuestionScroll;

    @FXML
    private Button Save_Edit_Question;

    @FXML
    private Button AddQuetionButton;

    @FXML
    private TextField Question_Points1;

    @FXML
    private TextField EditPoints;

    @FXML
    private TextField AddPoints;

    @FXML
    private Menu HomePage;

    @FXML
    private Menu home_page_clicked;

    @FXML
    private MenuItem view_questions_button;

    @FXML
    private MenuItem add_question_button;

    @FXML
    private MenuItem edit_question_button;

    @FXML
    private MenuItem builed_exam_button;

    @FXML
    private MenuItem prepare_exam_button;

    @FXML
    private MenuItem execute_exam;

    @FXML
    private MenuItem request_time_extention;

    @FXML
    private MenuItem approve_grades_button;

    @FXML
    private MenuItem view_grades_button;

    @FXML
    private TextField new_exam_code;

    @FXML
    private ChoiceBox<String> ExamPickScroll;
    @FXML
    private TextField StudentsNotes;
    @FXML
    private TextField TeacherNotes;

    @FXML
    private Button ShowExam;
    Exam exam;
    List<Question>NotQuestions;
List<Question>questions;
    @FXML
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        ExamPickScroll.setOnAction(this::ExamPicked);
        EditQuetionScroll.setOnAction(this::editQuestionPicked);

        ExamPickScroll.setItems(FXCollections.observableArrayList(this.app.getTeacher().getExamIdsInSubjects()));
    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        if (msg instanceof List<?> list) {
            System.out.println("simpleclient its list");

            if (!list.isEmpty()) {
                System.out.println("list not empty");
                Object firstElement = list.get(0);
                if (firstElement instanceof Question) {
                    List<Question> questionList = new ArrayList<>();
                    for (Object obj : list) {
                        questionList.add((Question) obj); // Type cast each object to Question


                    }
                    questions=questionList;
                    if(exam!=null)
                    exam.setQuestions(questionList);
                    List<String>ExamQuetionsIds=this.app.getQuestionIds(questionList);
                    EditQuetionScroll.setItems(FXCollections.observableArrayList(ExamQuetionsIds));
                    RemoveQuestionScroll.setItems(FXCollections.observableArrayList(ExamQuetionsIds));
                    SimpleClient.getClient().sendToServer("Subject:" + ExamPickScroll.getSelectionModel().getSelectedItem().substring(0, 2));



                }
            }
        }
        if (msg instanceof Subject) {
            NotQuestions=((Subject) msg).getQuestions();
            List<String> NotQuestionsIds=this.app.getQuestionIds(NotQuestions);
            NotQuestionsIds.removeAll(EditQuetionScroll.getItems());
            AddQuestionScroll.setItems(FXCollections.observableArrayList(NotQuestionsIds));


        }
            if (msg instanceof Exam) {
            exam = (Exam) msg;
            EditQuetionScroll.setItems(FXCollections.observableArrayList(exam.getQuestionIds()));
            RemoveQuestionScroll.setItems(FXCollections.observableArrayList(exam.getQuestionIds()));
                TeacherNotes.setText(exam.getInstructionsForTeacher());
                StudentsNotes.setText(exam.getInstructionsForStudents());
if(questions!=null)
    exam.setQuestions(questions);
                //SimpleClient.getClient().sendToServer("Questions_for_subject:" + ExamPickScroll.getSelectionModel().getSelectedItem().substring(0, 2));


        }
    }
    @FXML
    void ExamPicked(ActionEvent event) {
        try {
            SimpleClient.getClient().sendToServer("Exam:" + ExamPickScroll.getSelectionModel().getSelectedItem());

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            SimpleClient.getClient().sendToServer("Questions_for_exam:" + ExamPickScroll.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    @FXML
    void AddQuestion(ActionEvent event) {
        if(AddQuestionScroll.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("Please select a question to add");
            alert.showAndWait();
            return ;

        }
        if(AddPoints.getText().equals("")||!this.app.isNumeric(AddPoints.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("Please type a correct number of points(numbers)");
            alert.showAndWait();
            return ;

        }
        exam.addQuestion(exam.getSubject().GetQuestionWithId(AddQuestionScroll.getSelectionModel().getSelectedItem()),Integer.parseInt(AddPoints.getText()));


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
    void HomePageClicked(ActionEvent event) {

    }

    @FXML
    void RemoveQuestion(ActionEvent event) {
        if(RemoveQuestionScroll.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("Please select a question to remove");
            alert.showAndWait();
            return;
        }
        exam.removeQuestionById(RemoveQuestionScroll.getSelectionModel().getSelectedItem());
        EditQuetionScroll.getItems().remove(RemoveQuestionScroll.getSelectionModel().getSelectedItem());
        RemoveQuestionScroll.getItems().remove(RemoveQuestionScroll.getSelectionModel().getSelectedItem());
        AddQuestionScroll.getItems().add(RemoveQuestionScroll.getSelectionModel().getSelectedItem());


    }

    @FXML
    void SaveExam(ActionEvent event) {
exam.setId(new_exam_code.getText());
try {
    SimpleClient.getClient().sendToServer(exam);
} catch (IOException e) {
    e.printStackTrace();
}
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucess!");
        alert.setHeaderText("Sucess notification!");
        alert.setContentText("Exam Added Succesfully");
        alert.showAndWait();
        Platform.runLater(() -> {
            try {
                app.setWindowTitle("Teacher Menu ");
                app.setContent("teacher_menu");
            } catch (IOException e){e.printStackTrace();
            }

        });

    }
    @FXML
    void editQuestionPicked(ActionEvent event) {
        EditPoints.setText(String.valueOf(exam.getQuestionPointsById(EditQuetionScroll.getSelectionModel().getSelectedItem())));

    }
     @FXML
     void ShowExam() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Exam content");
        alert.setHeaderText(null);

        ScrollPane scrollPane = new ScrollPane();
        TextArea textArea = new TextArea();
        textArea.setText(exam.generateExamText());
        textArea.setEditable(false);
        scrollPane.setContent(textArea);

        alert.getDialogPane().setContent(scrollPane);

        alert.showAndWait();
    }


    @FXML
    void SaveEditedQuestion(ActionEvent event) {
        if(EditQuetionScroll.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("Please select a question to edit");
            alert.showAndWait();
            return;
        }
        if(EditPoints.getText().isEmpty()||!this.app.isNumeric(EditPoints.getText()))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("Please type a valid Points number for the question");
            alert.showAndWait();
            return;
        }

        exam.setQuestionPointsById(EditQuetionScroll.getSelectionModel().getSelectedItem(),Integer.parseInt(EditPoints.getText()));

    }



}
