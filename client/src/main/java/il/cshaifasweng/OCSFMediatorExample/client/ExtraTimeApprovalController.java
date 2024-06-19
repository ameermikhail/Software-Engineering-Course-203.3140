package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Principal;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;
import il.cshaifasweng.OCSFMediatorExample.entities.Time_Extention_request;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class ExtraTimeApprovalController extends BaseController{
List<Time_Extention_request>The_Requests;
Time_Extention_request curr;
    @FXML
    private Button Approve_Request;

    @FXML
    private ChoiceBox<String> Choose_Exam;
    @FXML
    private Button Decline_Request;

    @FXML
    private Button Save_Changings;

    @FXML
    private AnchorPane rootPane;
    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        if (msg instanceof List) {
            System.out.println("its list in entra");
            List<?> list = (List<?>) msg;
            List<String> timeExtensionStrings = new ArrayList<>();

            if (!list.isEmpty() && list.get(0) instanceof Time_Extention_request) {
System.out.println("its reached and not empty");
                List<Time_Extention_request>Requests=new ArrayList<>();
                for(Object obj:(List<?>)msg)
                {
                    Time_Extention_request timeRequest=(Time_Extention_request) obj;
                    int requestId = timeRequest.getId();
                    String examCode = Integer.toString(timeRequest.getPlannedExam().getId());
                    int timeToAdd = timeRequest.getTimeAddition();
                    Requests.add(timeRequest);
                    // Create the string in the desired format and add it to the list
                    String timeExtensionString = " ID: " + requestId + ", Exam Code: " + examCode +", Exam Name: "+timeRequest.getPlannedExam().getName()+ ", Time to Add: " + timeToAdd;
                    if(timeRequest.getIsdone()==0)timeExtensionStrings.add(timeExtensionString);



                }
                The_Requests=Requests;
System.out.println("requests assigned"+ String.valueOf(The_Requests.size()));
                if (Choose_Exam.getItems() == null) {
                    Choose_Exam.setItems(FXCollections.observableArrayList(timeExtensionStrings));
                } else {
                    Choose_Exam.getItems().setAll(timeExtensionStrings);
                }


            }
            else{
                System.out.println("list is empty !!!!");
            }
            }

            }

    @FXML
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
        SimpleClient.getClient().sendToServer("TimeRequests");
    this.Choose_Exam.setOnAction(this::DragDetected);}

    public Time_Extention_request GetRequestWithId(int id)
    {
        for(Time_Extention_request request:this.The_Requests) {
            System.out.println(request.getId());
            System.out.println("AAAa");
            System.out.println(id);

            if (request.getId() == id) return request;
        }
        return null;

    }
    @FXML
    void DragDetected(ActionEvent event) {
        System.out.println("meeow");
        if(Choose_Exam.getSelectionModel().getSelectedItem()==null)return;
        String Selected=Choose_Exam.getSelectionModel().getSelectedItem();
        Selected=Selected.substring(5,Selected.indexOf(","));
        System.out.println(Selected.length());

        curr=GetRequestWithId(Integer.parseInt(Selected));
        System.out.println(Selected);
//extract address
    }
    public void removeOld()
    {
        String Selected=Choose_Exam.getSelectionModel().getSelectedItem();
        Choose_Exam.getItems().remove(Selected);

    }
    @FXML
    void ApproveRequest(ActionEvent event) throws IOException {
        if(curr==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("please select A request");
            alert.showAndWait();
            return ;

        }
        try{
SimpleClient.getClient().sendToServer("addtime:"+curr.getId()+":"+curr.getPlannedExam().getId()+":"+curr.getTimeAddition());
            SimpleClient.getClient().sendToServer("RTR"+curr.getId());

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        //curr.Accept();
        removeOld();
        //SimpleClient.getClient().sendToServer("RTR"+curr.getId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucess!");
        alert.setHeaderText("Sucess notification!");
        alert.setContentText("Request  Approved Succesfully");
        alert.showAndWait();
        Platform.runLater(() -> {
            try {
                app.setWindowTitle("Principal Menu ");
                app.setContent("principal_menu");
            } catch (IOException e){e.printStackTrace();
            }

        });
    }

    @FXML
    void DeclineRequest(ActionEvent event) throws IOException {
        if(curr==null)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("please selet A request");
            alert.showAndWait();
            return ;

        }
        removeOld();
        SimpleClient.getClient().sendToServer("RTR"+curr.getId());


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
