package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import javafx.application.Platform;
import javafx.fxml.FXML;
import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.GradedExam;
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

public class ApproveGradeController extends BaseController{

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button edit_grade_button;

    @FXML
    private ScrollPane exam_content;

    @FXML
    private TextField grade_textFeild;

    @FXML
    private TextField notes_textFeild;

    @FXML
    private TextField Explanation_TF;

    @FXML
    private Button Approve;

    @FXML
    private ChoiceBox<String> choosen_exam_choice_box;

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
    public GradedExam curr;
    public List<GradedExam> ExamsToGrade;
    int Isedited=0;
    @FXML
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
       // ExamsToGrade=filterByCorrected(this.app.getTeacher().getGradedExams(),0);
        choosen_exam_choice_box.setOnAction(this::ExamDetected);
        try {
            SimpleClient.getClient().sendToServer("AllGrades:"+String.valueOf(this.app.getTeacher().getId()));
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public List<String>DetailsofGrade()
    {   List<String>details=new ArrayList<>();
        String tmp;
        for(GradedExam exam :this.ExamsToGrade)
        {
            tmp=exam.getStudent().getId()+":"+exam.getStudent().getName()+":"+exam.getPlannedExam().getId()+":"+exam.getId();
           details.add(tmp);
        }
       return details;

    }
    public static List<GradedExam> filterByCorrected(List<GradedExam> gradedExams, int correctedValue) {
        List<GradedExam> filteredList = new ArrayList<>();
        for (GradedExam exam : gradedExams) {
            if (exam.getCorrected() == correctedValue) {
                filteredList.add(exam);
            }
        }
        return filteredList;
    }
    public  GradedExam findGradedExamByStudentAndExamIdd( int studentId, int examId,int examindex) {
        for (GradedExam gradedExam : ExamsToGrade) {
            System.out.println(String.valueOf(gradedExam.getStudent().getId())+"ZZ"+String.valueOf(gradedExam.getPlannedExam().getId()));
            if (gradedExam.getStudent().getId() == studentId && gradedExam.getPlannedExam().getId() == examId&&gradedExam.getId()==examindex) {
                return gradedExam;
            }
        }
        return null; // Return null if no matching graded exam is found
    }

    @FXML
    void ExamDetected(ActionEvent event) {
        if(choosen_exam_choice_box.getSelectionModel().getSelectedItem()==null)return;
        Isedited=0;
        System.out.println(choosen_exam_choice_box.getSelectionModel().getSelectedItem());
        String[] parts = choosen_exam_choice_box.getSelectionModel().getSelectedItem().split(":");
        String id = parts[0];
        String name = parts[1];
        String examId = parts[2];
        String examindex=parts[3];
        System.out.println("ff"+examindex);
        System.out.println(id+"Z"+name+"z"+examId);
        System.out.println("number:"+String.valueOf(ExamsToGrade.size()));
    curr=findGradedExamByStudentAndExamIdd(Integer.parseInt(id),Integer.parseInt(examId),Integer.parseInt(examindex));
    //curr.gradeExam();
        TextArea examTextArea = new TextArea();
        examTextArea.setText(curr.generateExamText());
        exam_content.setContent(examTextArea);
        grade_textFeild.setText(String.valueOf(curr.getGrade()));
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
    void ApproveGrade(ActionEvent event) {
        if (choosen_exam_choice_box.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("please select an exam to correct");
            alert.showAndWait();
        }
        if (Isedited == 1 && notes_textFeild.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("please provide explanation to the grade change");
            alert.showAndWait();
        }
        curr.setCorrected(1);

        if (Isedited == 1) {
            curr.setGrade(Integer.parseInt(grade_textFeild.getText()));
            curr.setGradeChangeExplanation(notes_textFeild.getText());
        }
        curr.setCorrected(1);
        try{
            SimpleClient.getClient().sendToServer(curr);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        //curr.getPlannedExam().getGradedExams().add(curr);
        choosen_exam_choice_box.getItems().remove(choosen_exam_choice_box.getSelectionModel().getSelectedItem());
        // Clear the selection
        choosen_exam_choice_box.getSelectionModel().clearSelection();
        grade_textFeild.setEditable(false);
        Isedited=0;
        TextArea examTextArea = new TextArea();
        examTextArea.setText("");

        exam_content.setContent(examTextArea);
        grade_textFeild.setText((""));
        notes_textFeild.setText("");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucess!");
        alert.setHeaderText("Sucess notification!");
        alert.setContentText("Grade Approved Succesfully");
        alert.showAndWait();

            try {
                app.setWindowTitle("Teacher Menu ");
                app.setContent("teacher_menu");
            } catch (IOException e){e.printStackTrace();
            }



    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        if (msg instanceof List<?>) {
            List<?> list = (List<?>) msg;
            System.out.println("approvegrades its list");

            if (!list.isEmpty()) {
                System.out.println("list not empty");
                Object firstElement = list.get(0);
                if (firstElement instanceof GradedExam) {
                    ExamsToGrade = new ArrayList<>();

                    for (Object obj : list) {
                        if (obj instanceof GradedExam) {
                            ExamsToGrade.add((GradedExam) obj);
                        }
                    }

                    List<String> gradesToText = DetailsofGrade();
                    choosen_exam_choice_box.setItems(FXCollections.observableArrayList(gradesToText));
                }
            }
        }
    }

    @FXML
    void EditGradee(ActionEvent event) {

grade_textFeild.setEditable(true);
Isedited=1;
    }

}


