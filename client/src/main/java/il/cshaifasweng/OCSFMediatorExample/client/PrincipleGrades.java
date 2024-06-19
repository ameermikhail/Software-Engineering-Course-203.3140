package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.GradedExam;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javassist.Loader;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrincipleGrades extends BaseController {
    @FXML
    private TableView<GradedExam> gradedExamsTable;

    @FXML
    private TableColumn<GradedExam, String> examIdColumn;

    @FXML
    private TableColumn<GradedExam, String> studentIdColumn;

    @FXML
    private TableColumn<GradedExam, String> studentNameColumn;

    @FXML
    private TableColumn<GradedExam, Double> gradeColumn;
    List<GradedExam> gradedExamsList;
    @FXML
    private TableColumn<GradedExam, Integer> teacherIdColumn;
    public void Arrived()
    {

        // Convert the list to an ObservableList to display in the table
        ObservableList<GradedExam> gradedExamsData = FXCollections.observableArrayList(gradedExamsList);

        // Set the data to the table
        gradedExamsTable.setItems(gradedExamsData);

        // Bind each column to the respective attributes in the GradedExam class
        examIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getId())));
        studentIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStudent().getId())));
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getName()));
        gradeColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getGrade()).asObject());
        teacherIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTeacher().getId()).asObject());
    }


    public void initialize() throws IOException {
        try {
            SimpleClient.getClient().sendToServer("AllGrads");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        if (msg instanceof List<?>) {
            List<?> list = (List<?>) msg;
            System.out.println("otsss list"+String.valueOf(list.size()));

            if (!list.isEmpty()) {
                List<GradedExam>gradedExams=new ArrayList<>();
                Object firstElement = list.get(0);
                if (firstElement instanceof GradedExam) {
                    for(Object obj:list)
                    {
                        gradedExams.add((GradedExam) obj);
                    }
                    gradedExamsList=gradedExams;
                    System.out.println("xx");

                    System.out.println(gradedExamsList.size());
                    Arrived();
                }
            }
        }
    }
    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        System.out.println("it was clicked");
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        System.out.println(buttonId);
        String idWithoutSuffix = buttonId.replace("_button", "");
        System.out.println(idWithoutSuffix);
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }

    @FXML
    void HomePageClicked(ActionEvent event) throws IOException {
        app.setWindowTitle("principal Menu");
        app.setContent("principal_menu");
    }


}

