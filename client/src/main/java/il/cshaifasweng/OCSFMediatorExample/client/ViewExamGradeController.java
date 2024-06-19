package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.GradedExam;
import il.cshaifasweng.OCSFMediatorExample.entities.Question;
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

public class ViewExamGradeController extends BaseController{
List<GradedExam> exams;
GradedExam curr;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ChoiceBox<String> ExamChoose;
    @FXML
    private Button View_Exam;

    @FXML
    private ScrollPane exam_content;

    @FXML
    private Label GradeLAbel;
    public  List<GradedExam> filterByCorrected(List<GradedExam> gradedExams, int correctedValue) {
        List<GradedExam> filteredList = new ArrayList<>();
        for (GradedExam exam : gradedExams) {
            if (exam.getCorrected() == correctedValue&&exam.getStudent().getId()==this.app.getStudent().getId()) {
                filteredList.add(exam);
            }
        }
        return filteredList;
    }

    @FXML
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        ExamChoose.setOnAction(this::ExamPicked);

        try {
            SimpleClient.getClient().sendToServer("Gradesss:" + this.app.getStudent().getId());
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    @FXML
    void ExamPicked(ActionEvent event) {
     curr=this.app.findGradedExamById(exams,Integer.parseInt(ExamChoose.getSelectionModel().getSelectedItem()));
     GradeLAbel.setText("Grade:"+String.valueOf(curr.getGrade()));
        TextArea examTextArea = new TextArea();
        examTextArea.setText(curr.generateExamText());
        exam_content.setContent(examTextArea);
    }

    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        if (msg instanceof List<?> list) {
            if (!list.isEmpty()) {
                List<GradedExam>finaal=new ArrayList<>();
                System.out.println("listisze:" + String.valueOf(list.size()));
                System.out.println("zzzzz");

                Object firstElement = list.get(0);
                if (firstElement instanceof GradedExam) {
                  for(Object obj:list)
                  {
                      finaal.add((GradedExam) obj);
                  }
                  exams=finaal;
                    exams=filterByCorrected(exams,1);
System.out.println(exams.size());
                    ExamChoose.setItems(FXCollections.observableArrayList(this.app.getGradedExamIds(exams)));



                }

            }
        }
    }
    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        System.out.println(idWithoutSuffix);
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }

}
