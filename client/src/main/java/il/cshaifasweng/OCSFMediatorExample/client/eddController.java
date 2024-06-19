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
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javax.xml.bind.DatatypeConverter.parseTime;

public class eddController extends BaseController {

Subject TheSubject;
        @FXML
        private AnchorPane rootPane;

        @FXML
        private ChoiceBox<String> Subject_ID_Scroll;

        @FXML
        private Button finish_button;

        @FXML
        private ChoiceBox<String> Course_ID_scroll;

        @FXML
        private ChoiceBox<String> ExamTypeDrop;

        @FXML
        private TextField ExecutionCode;

        @FXML
        private Menu HomePage;

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
        private Button show_exam_button;
        @FXML
        private MenuItem approve_grades_button;

        @FXML
        private MenuItem view_grades_button;

        @FXML
        private TextField ExamCode;
        @FXML
        private DatePicker DatePick;
List<Course>courses;
Course course;
Subject subject;
        @FXML
        private TextField startingTime;
        @FXML
        public void initialize() throws IOException {
                EventBus.getDefault().register(this);
                ShowSubjects();
                Subject_ID_Scroll.setOnAction(this::SubjectDetected);
                Course_ID_scroll.setOnAction(this::CourseDetected);
                ExamTypeDrop.setItems(FXCollections.observableArrayList("Manual","Online"));
        }
        @FXML
        void CourseDetected(ActionEvent event)  {
if(Course_ID_scroll.getSelectionModel().getSelectedItem()==null)return;
                        this.course=this.app.getCourseByKey(courses,Integer.parseInt(Course_ID_scroll.getValue().split(" ")[0]));

        }
        @FXML
        void ShowExamClicked(ActionEvent event) {
                if (Subject_ID_Scroll.getValue() == null||Course_ID_scroll.getValue()==null) {
                        showErrorDialog("please choose subject and course !");
                        return;
                }

                Exam exam = this.app.getTeacher().getsubjectWithNumber(Integer.parseInt(Subject_ID_Scroll.getValue().split(" ")[0])).GetExamWithId(String.valueOf(subject.getid()) + String.valueOf(course.getCourse_id()) + ExamCode.getText());

                if (exam == null) {
                        showErrorDialog("Exam doesn't exist!");
                } else {
                        showExamInScrollPane(exam.generateExamText());
                }
        }

        private void showErrorDialog(String message) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("An Error Occurred!");
                alert.setContentText(message);
                alert.showAndWait();
        }

        private void showExamInScrollPane(String examText) {
                TextArea textArea = new TextArea(examText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setPrefSize(400, 400); // Adjust the size as needed
                scrollPane.setContent(textArea);

                // Create custom dialog
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Exam Content");
                alert.setHeaderText(null);
                alert.setContentText("Below is the exam content:");

                // Set up the custom content
                alert.getDialogPane().setExpandableContent(scrollPane);
                alert.getDialogPane().setExpanded(true);

                alert.showAndWait();
        }

        @FXML
        void Handle_menu_clicked(ActionEvent event) throws IOException {
                MenuItem clickedMenu = (MenuItem) event.getSource();
                String buttonId = clickedMenu.getId();
                String idWithoutSuffix = buttonId.replace("_button", "");
                app.setWindowTitle(idWithoutSuffix);
                app.setContent(idWithoutSuffix);


        }
        @Subscribe
        public void handleEvent(Object msg) throws IOException {

                if (msg instanceof List<?> list) {
                        System.out.println("simpleclient its list");

                        if (!list.isEmpty()) {
                                System.out.println("list not empty");
                                Object firstElement = list.get(0);
                                if (firstElement instanceof Course) {
                                        List<Course> coursesList = new ArrayList<>();
                                        for (Object obj : list) {
                                                if (obj instanceof Course) {
                                                        coursesList.add((Course) obj); // Type cast each object to Question


                                                }
                                        }
                                        this.courses = coursesList;
                                        Course_ID_scroll.setItems(FXCollections.observableArrayList(this.app.getCourseIds(courses)));

                                }

                        }
                }

        }





        public void ShowSubjects()
        {
                List<String> subjects_names = this.app.getTeacher().GetSubjectsNames();
                Subject_ID_Scroll.setItems(FXCollections.observableArrayList(subjects_names));


        }
       @FXML public void Finish() {
               try {
                       Teacher teacher = this.app.getTeacher();
                       System.out.println(String.valueOf(subject.getid())+String.valueOf(course.getCourse_id())+ExamCode.getText());
                       System.out.println("XX");
                       System.out.println();

                       Exam exam = teacher.getsubjectWithNumber(Integer.parseInt(Subject_ID_Scroll.getValue().split(" ")[0])).GetExamWithId(String.valueOf(subject.getid())+String.valueOf(course.getCourse_id())+ExamCode.getText());
                       if(exam==null)
                       {
                               Alert alert = new Alert(Alert.AlertType.INFORMATION);
                               alert.setTitle("Error!");
                               alert.setHeaderText("Error notification!");
                               alert.setContentText("Exam doesnt exist !");
                               alert.showAndWait();
return;
                       }

                       System.out.println(exam.generateExamText());
                       LocalDate date = DatePick.getValue();
                       LocalTime time = startingTime.getText().isEmpty() ? null : parseTime(startingTime.getText());
                       ;
                       if (date != null && time != null) {
                               LocalDateTime dateTime = LocalDateTime.of(date, time);

                               PlannedExam planned = new PlannedExam(Integer.parseInt(ExecutionCode.getText()),teacher, exam,subject,course, dateTime, ExamTypeDrop.getValue());
                                for(PlannedExam pln:  this.app.getTeacher().getPlannedExams())
                                {
                                        if(pln.getId()==Integer.parseInt(ExecutionCode.getText())) {
                                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                                alert.setTitle("Failure!");
                                                alert.setHeaderText("Failure notification!");
                                                alert.setContentText("Execution code already exists!");
                                                alert.showAndWait();
                                                return;
                                        }

                                }
                               SimpleClient.getClient().sendToServer(planned);
                               this.app.getTeacher().getPlannedExams().add(planned);
                               Alert alert = new Alert(Alert.AlertType.INFORMATION);
                               alert.setTitle("Sucess!");
                               alert.setHeaderText("Sucess notification!");
                               alert.setContentText("Exam Planned Succesfully");
                               alert.showAndWait();
                               Platform.runLater(() -> {
                                       try {
                                               app.setWindowTitle("Teacher Menu ");
                                               app.setContent("teacher_menu");
                                       } catch (IOException e){e.printStackTrace();
                                       }

                               });

                       }
               } catch (IOException e) {
                       e.printStackTrace();}



       }


               private LocalTime parseTime(String timeString) {
                       DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                       try {
                               return LocalTime.parse(timeString, timeFormatter);
                       } catch (Exception e) {
                               return null; // Return null if the timeString cannot be parsed
                       }
               }

        void SubjectDetected(ActionEvent event) {
                this.subject = this.app.getTeacher().getsubjectWithNumber(Integer.parseInt(Subject_ID_Scroll.getValue().split(" ")[0]));
                TheSubject=subject;
                try {
                        SimpleClient.getClient().sendToServer("Courses_for_subject:" + Subject_ID_Scroll.getValue().split(" ")[0]);
                }catch (IOException e)
                {
                        e.printStackTrace();
                }
        }

                public int checkFeilds() {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Wrong/Missing Feilds");
                        alert.setHeaderText("Please correct the follwoing issues:");

                        List<TextField> textFieldList = new ArrayList<>();
                        int feildsGood = 1;
                        if (Subject_ID_Scroll.getValue() == null) {
                                alert.setContentText("Please select a subject");
                                alert.showAndWait();
                                return 0;
                        }
                        if (Course_ID_scroll.getValue() == null) {
                                alert.setContentText("Please select a Course");
                                alert.showAndWait();
                                return 0;
                        }
                        if (ExamTypeDrop.getValue() == null) {
                                alert.setContentText("Please select an Exam type");
                                alert.showAndWait();
                                return 0;
                        }
                        if (startingTime.getText().equals("")) {
                                alert.setContentText("Please type Starting time");
                                alert.showAndWait();
                                return 0;
                        }
                        if (ExecutionCode.getText().equals("")) {
                                alert.setContentText("Please type Execution Code");
                                alert.showAndWait();
                                return 0;
                        }
                        if (ExecutionCode.getText().length() != 4 || this.app.checkIfTextIsNumber(ExecutionCode.getText()) == 0) {
                                alert.setContentText("Please type 4 digits Execution Code");
                                alert.showAndWait();
                                return 0;
                        }

                        String curr_Qnumber = Subject_ID_Scroll.getValue() + Course_ID_scroll.getValue() + ExamCode.getText();
                        if (TheSubject.IsPlannedExamIdTaken(curr_Qnumber) == 1) {
                                alert.setContentText("Execution code already taken please pick a different one");
                                alert.showAndWait();
                                return 0;
                        }
                        if (TheSubject.IsExamIdTaken(ExamCode.getText()) == 0) {
                                alert.setContentText("Invalid Examcode , this exam doesnt exist");
                                alert.showAndWait();
                                return 0;
                        }
                        if (!isValidTimeFormat(startingTime.getText())) {
                                alert.setContentText("Invalid Starting Time");
                                alert.showAndWait();
                                return 0;
                        }
                        if (DatePick.getValue() == null) {
                                alert.setContentText("please select a date");
                                alert.showAndWait();
                                return 0;

                        }
                        return 1;
                }
        public static boolean isValidTimeFormat(String time) {
                // Regular expression for the format HH:mm
                String timeRegex = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

                return time.matches(timeRegex);
        }



}


