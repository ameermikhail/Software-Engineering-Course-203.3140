package il.cshaifasweng.OCSFMediatorExample.client;

import java.io.IOException;

import il.cshaifasweng.OCSFMediatorExample.entities.Principal;
import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.Teacher;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class SecondaryController extends BaseController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label wrongDetailsLabel;

    @FXML
    public void initialize() throws IOException {
        EventBus.getDefault().register(this);
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Perform login authentication logic here
        // You can check the username and password against a database or any other authentication mechanism
        SimpleClient.getClient().sendToServer("login:" + username + ":" + password);
        System.out.println("meow23");
    }

    @FXML
    public void switchToPrimary() throws IOException {
        Platform.runLater(() -> {
            app.setWindowTitle("title");
            try {
                app.setContent("test");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    @FXML
    public void switchToMenu(String type) throws IOException {

        Platform.runLater(() -> {
            app.setWindowTitle(type);
            try {
                app.setContent(type);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }


    @Subscribe
    public void handleEvent(Object msg) throws IOException {
        System.out.println("handle login reached");
        if (msg instanceof String) {
            if (((String) msg).equals("wrong")) {
                System.out.println("hiii");
            }
            System.out.println((String) msg);
            if (((String) msg).equals("wrong")) {
                Platform.runLater(() -> {
                    // This code is executed on the JavaFX application thread
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Problem::");
                    alert.setContentText("Wrong username or password !");
                    alert.showAndWait();
                });
            }


            if (((String) msg).equals("Client Is Already Connected!")) {
                Platform.runLater(() -> {

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error!");
                    alert.setHeaderText("Problem:");
                    alert.setContentText("User is already connected!");
                    alert.showAndWait();
                });

            }
        }



        if(msg instanceof Student student) {
            app.setType("student");
            app.setId(student.getId());
            app.setStudent(student);

            switchToMenu("student_menu");
        }
        if(msg instanceof Teacher teacher) {
            System.out.println("teacher in secpnmdary gotten");

            app.setType("teacher");
            app.setId(teacher.getId());
            app.setTeacher(teacher);
            switchToMenu("teacher_menu");
        }
        if(msg instanceof Principal principal) {
System.out.println("principal in secpnmdary gotten");
            app.setType("principal");
            app.setId(principal.getId());
            app.setPrincipal(principal);
            //SimpleClient.getClient().sendToServer("setlogin:"+this.app.getId());
            switchToMenu("principal_menu");
        }
        if(msg instanceof Student)
        {
            Student student2=(Student)msg;

            app.setType("student");
            app.setId(student2.getId());
            app.setStudent(student2);
            switchToMenu("student_menu");

        }
    }
    public SecondaryController(App app) {
        setApp(app);
    }

}
