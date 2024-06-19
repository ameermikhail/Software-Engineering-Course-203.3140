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
import javafx.scene.text.Text;
import javassist.Loader;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddExamController extends BaseController {
    int QuestionsCount = 1;
    Subject subject;
    Course course;
    Question curr_Question;
    Exam currExam;
    List<Integer> points;
    List<Question> questions;
    List<Course> courses;
    List<Subject> subjects;
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button Add_Question;

    @FXML
    private ChoiceBox<String> Subject_ID_Scroll;

    @FXML
    private Button Save_New_Exam;

    @FXML
    private ChoiceBox<String> Course_ID_scroll;

    @FXML
    private ChoiceBox<String> Choose_Question;

    @FXML
    private ChoiceBox<String> ExamTypeDrop;

    @FXML
    private TextField Exam_Code;

    @FXML
    private TextField Question_Points;

    @FXML
    private TextField TeacherNotesFeild;

    @FXML
    private Label Error_Note;
    @FXML
    private Label CqLabel;
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
    private MenuItem approve_grades_button;

    @FXML
    private MenuItem view_grades_button;

    @FXML
    private TextField ExamDurationFeild;

    @FXML
    private TextField StudentsNotesFeild;

    @FXML
    private ScrollPane QuestionViewScroll;

    @FXML
    void AddQuestion(ActionEvent event) {
        if (points == null) points = new ArrayList<>();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        if (Choose_Question.getValue().equals("")) {
            alert.setContentText("Please select a question");
            alert.showAndWait();
            return;
        }
        if (Question_Points.getText().equals("")) {
            alert.setContentText("Please fill the points feild");
            alert.showAndWait();
            return;
        }
        if (checkIfTextIsNumber(Question_Points.getText()) == 0) {
            alert.setContentText("Please fill the points feild with numbers only");
            alert.showAndWait();
            return;
        }


        currExam.AddQuestion(GetQuestionWithId(questions, Choose_Question.getValue().split(" ")[0]), Integer.parseInt(Question_Points.getText()));

        CqLabel.setText("Choose Question" + QuestionsCount + " :");
        QuestionsCount++;
    }

    @FXML
    void SaveNewExam(ActionEvent event) throws IOException {
        SimpleClient.getClient().sendToServer(currExam);
        this.app.getTeacher().addExam(currExam);
        this.app.getTeacher().getsubjectWithNumber(subject.getid()).getExams().add(currExam);
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
    public void initialize()  {
        EventBus.getDefault().register(this);
        ShowSubjects();
        ExamTypeDrop.setItems(FXCollections.observableArrayList("Manual", "Online"));
        try {
            SimpleClient.getClient().sendToServer("Subjects_for_teacher:" + this.app.getTeacher().getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Subject_ID_Scroll.setOnAction(this::SubjectDetected);
        Course_ID_scroll.setOnAction(this::CourseDetected);
         Choose_Question.setOnAction(this::ChooseQuestionDone);
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
        if (ExamDurationFeild.getText().equals("")) {
            alert.setContentText("Please type Exam Duration");
            alert.showAndWait();
            return 0;
        }
        if (Exam_Code.getText().equals("")) {
            alert.setContentText("Please type Exam Duration");
            alert.showAndWait();
            return 0;
        }
        String curr_Qnumber = subject.getId() + course.getCourse_id() + Exam_Code.getText();
       /* if (subject.IsExamIdTaken(curr_Qnumber) == 1) {
            alert.setContentText("ExamCode already taken please pick a different one");
            alert.showAndWait();
            return 0;
        }*/
        return 1;
    }

    @FXML
    void DefineExamClicked(ActionEvent event) {
        if (checkFeilds() == 1) {

            String subjectId = Subject_ID_Scroll.getValue();
            String courseId = Course_ID_scroll.getValue();
            String chosenQuestion = Choose_Question.getValue();
            String examType = ExamTypeDrop.getValue();
            String examCode = Exam_Code.getText();
            String questionPoints = Question_Points.getText();
            String teacherNotes = TeacherNotesFeild.getText();
            String studentNotes = StudentsNotesFeild.getText();
            String idd = subjectId.split(" ")[0] + courseId.split(" ")[0] + examCode;
            int duration=Integer.parseInt(ExamDurationFeild.getText());
            Course course = getCourseWithID(courses, courseId.split(" ")[0]);
            currExam = new Exam(idd, studentNotes, teacherNotes, this.app.getTeacher(), subject, course,duration);
            CqLabel.setText("Choose Question " + QuestionsCount);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucess!");
            alert.setHeaderText("Exam Defined succedfully!");
            alert.setContentText("Please add questions then press finish");
            alert.showAndWait();


        }
    }

    public Course getCourseWithID(List<Course> courses, String courseId) {
        if (courses.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong/Missing Feilds");
            alert.setHeaderText("Please correct the follwoing issues:");
            alert.setContentText("courses empty");
            alert.showAndWait();


        }
        for (Course coursexx : courses) {
            if (coursexx.getCourse_id() == Integer.parseInt(courseId)) {
                return course;
            }
        }
        return null;
    }

    @FXML
    void ggg(MouseEvent event) {
System.out.println("zzzzzzzzzz");
    }
    @FXML
    void CourseDetected(ActionEvent event) {
        if(Course_ID_scroll.getValue()==null)return;
this.course=getCourseByKey(courses,Integer.parseInt(Course_ID_scroll.getValue().split(" ")[0]));
    }
    @FXML
    void SubjectDetected(ActionEvent event) {
if(subjects==null)return;
if(subjects.isEmpty())return;

        System.out.println("subject detected");
        this.subject = getsubjectWithNumber(Integer.parseInt(Subject_ID_Scroll.getValue().split(" ")[0]));
        System.out.println("subject brought");

        //this.app.client.openConnection();
        try {
            this.app.client.sendToServer("Courses_for_subject:" + Subject_ID_Scroll.getValue().split(" ")[0]);
            this.app.client.sendToServer(("Questions_for_subject:" + Subject_ID_Scroll.getValue().split(" ")[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Course getCourseByKey(List<Course> courses, int key) {
        for (Course course : courses) {
            if (course.getCourse_id() == key) {
                return course;
            }
        }
        return null; // Return null if no course with the given key is found
    }


    public Subject getsubjectWithNumber(int subjectNumber) {

        System.out.println("sizE:" + String.valueOf(subjects.size()));
        for (Subject subject1 : subjects) {
System.out.println(String.valueOf(subject1.getId())+String.valueOf(subjectNumber));
            if (subject1.getId() == subjectNumber) return subject1;
        }
        return null;
    }

    @FXML
    void ChooseQuestionDone(ActionEvent event) {

        this.curr_Question = GetQuestionWithId(questions, Choose_Question.getValue().split(" ")[0]);
        TextArea textArea = new TextArea();
        QuestionViewScroll.setContent(textArea);
        textArea.setText(curr_Question.QuestionToText());
    }

    public Question GetQuestionWithId(List<Question> questions, String id) {
        for (Question question : questions) {
            if (question.getQuestionId() == Integer.parseInt(id))
                return question;
        }
        return null;
    }

    public void ShowSubjects() {
        List<String> subjects_names = this.app.getTeacher().GetSubjectsNamesandIds();
        if (!subjects_names.isEmpty())
            Subject_ID_Scroll.setItems(FXCollections.observableArrayList(subjects_names));

    }

    public void ShowCourses() {
        List<String> courses_names = this.app.getTeacher().getCoursesIds();
        if (!courses_names.isEmpty())
            Subject_ID_Scroll.setItems(FXCollections.observableArrayList(courses_names));

    }

    public static int checkIfTextIsNumber(String text) {
        // Check if the text is null or empty
        if (text == null || text.isEmpty()) {
            return 0;
        }

        // Iterate through each character in the text
        for (char c : text.toCharArray()) {
            // If any character is not a digit, return 0
            if (!Character.isDigit(c)) {
                return 0;
            }
        }

        // If all characters are digits, return 1
        return 1;
    }

    @FXML
    void Handle_menu_clicked(ActionEvent event) throws IOException {
        MenuItem clickedMenu = (MenuItem) event.getSource();
        String buttonId = clickedMenu.getId();
        String idWithoutSuffix = buttonId.replace("_button", "");
        app.setWindowTitle(idWithoutSuffix);
        app.setContent(idWithoutSuffix);


    }

    public List<String> getCourseIds(List<Course> courses) {
        List<String> courseIds = new ArrayList<>();
        for (Course course : courses) {
            courseIds.add(String.valueOf(course.getCourse_id())+" "+course.getName());
        }
        return courseIds;
    }

    public List<String> getQuestionIds(List<Question> questions) {
        List<String> questionIds = new ArrayList<>();
        for (Question question : questions) {
            questionIds.add(String.valueOf(question.getQuestionId()+" "+question.getQuestionText()));
        }
        return questionIds;
    }

    @Subscribe
    public void handleEvent(Object msg) {
        try {
            System.out.println("handle login reached");
            if (msg instanceof String) {
                if (((String) msg).equals("exam_add_success")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Exam Addition result");
                    alert.setHeaderText("Exam Addition result");

                    alert.setContentText("Exam was added succesfully");
                    alert.showAndWait();
                    app.setWindowTitle("teacher_menu");
                    app.setContent("teacher_menu");
                }


            }
            if (msg instanceof Question) {
                Text text = new Text();
                text.setText(((Question) msg).getQuestionText());
                QuestionViewScroll.setContent(text);
                curr_Question=(Question) msg;

            }

                if (msg instanceof List<?> list) {
                if (!list.isEmpty()) {
                    System.out.println("listisze:"+String.valueOf(list.size()));
                    Object firstElement = list.get(0);
                    if (firstElement instanceof Question) {
                        List<Question> questionList = new ArrayList<>();
                        for (Object obj : list) {
                            if (obj instanceof Question) {
                                questionList.add((Question) obj); // Type cast each object to Question
                            }
                        }

                        this.questions = questionList;
                        Choose_Question.setItems(FXCollections.observableArrayList(getQuestionIds(questionList)));


                    }
                    if (firstElement instanceof Course) {
                        List<Course> coursesList = new ArrayList<>();
                        for (Object obj : list) {
                            if (obj instanceof Course) {
                                coursesList.add((Course) obj); // Type cast each object to Question


                            }
                        }
                        this.courses = coursesList;
                        Course_ID_scroll.setItems(FXCollections.observableArrayList(getCourseIds(courses)));


                    }
                    if (firstElement instanceof Subject) {
                        List<Subject> SubjectsList = new ArrayList<>();
                        for (Object obj : list) {
                                SubjectsList.add((Subject) obj); // Type cast each object to Question

                        }
                        this.subjects = SubjectsList;
                        System.out.println("subjects set");
                        System.out.println(this.subjects.get(0).getId());

                    }

                }
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exam Addition result");
            alert.setHeaderText("Exam Addition result");

            alert.setContentText(e.getMessage());
            alert.showAndWait();

            e.printStackTrace();


        }


    }
}

