package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * JavaFX App
 */
public class App extends Application {
    private Stage appstage;
    private static Scene scene;
    public SimpleClient client;
    private int id;
    public String Type;
    public Teacher teacher;
    public Student student;
    public Principal principal;
public PlannedExam plannedExam;



    public String getType() {
        return Type;
    }
public Stage getAppstage()
{

    return this.appstage;
}
    public void setType(String type) {
        Type = type;
    }
    public il.cshaifasweng.OCSFMediatorExample.entities.PlannedExam getPlannedExam() {
        return plannedExam;
    }

    public void setPlannedExam(il.cshaifasweng.OCSFMediatorExample.entities.PlannedExam plannedExam) {
        this.plannedExam = plannedExam;
    }
    @Override
    public void start(Stage stage) throws IOException {
        this.appstage = stage; // Set the stage reference for this instance

        client = SimpleClient.getClient();
        client.openConnection();
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("secondary" + ".fxml"));
        SecondaryController x=new SecondaryController(this);
        fxmlLoader.setController(x);
        scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }
    public List<GradedExam> filterByPlannedExamId(List<GradedExam> gradedExams, int id) {
        List<GradedExam>grds=new ArrayList<>();
for(GradedExam graded:gradedExams)
{
    if (graded.getPlannedExam().getId()==id)
        grds.add(graded);
}
return grds;


    }
    public String GetWindowsTitle() {
        return appstage.getTitle();
    }

    public void setWindowTitle(String title) {
        appstage.setTitle(title);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    public List<String> getCourseIdsOnly(List<Course> courses) {
        List<String> courseIds = new ArrayList<>();
        for (Course course : courses) {
            courseIds.add(String.valueOf(course.getCourse_id()));
        }
        return courseIds;
    }

    public List<String> getCourseIds(List<Course> courses) {
        List<String> courseIds = new ArrayList<>();
        for (Course course : courses) {
            courseIds.add(String.valueOf(course.getCourse_id()+" "+course.getName()));
        }
        return courseIds;
    }
    public Course getCourseByKey(List<Course> courses, int key) {
        for (Course course : courses) {
            if (course.getCourse_id() == key) {
                return course;
            }
        }
        return null; // Return null if no course with the given key is found
    }
    public  List<GradedExam> getFinishedGradedExams(List<Exam> exams) {
        List<GradedExam> finishedGradedExams = new ArrayList<>();
        for (Exam exam : exams) {
            for (PlannedExam pl : exam.getPlannedExams()) {
                for(GradedExam gradedExam:pl.getGradedExams())
                    if (gradedExam.getCorrected() == 1) {
                        finishedGradedExams.add(gradedExam);
                    }
            }
        }
        return finishedGradedExams;
    }

    public List<String> getQuestionIds(List<Question> questions) {
        List<String> questionIds = new ArrayList<>();
        for (Question question : questions) {
            questionIds.add(String.valueOf(question.getQuestionId()));
        }
        return questionIds;
    }
    public List<String> getGradedExamIds(List<GradedExam> gradedExams) {
        List<String> examIds = new ArrayList<>();
        for (GradedExam gradedExam : gradedExams) {
            if(!examIds.contains(String.valueOf(gradedExam.getPlannedExam().getId())))
            examIds.add(String.valueOf(gradedExam.getPlannedExam().getId()));
        }
        return examIds;
    }

    public Principal getPrincipal() {
        return principal;
    }
    public  boolean isNumeric(String text) {
        if (text == null || text.isEmpty()) {
            return false; // The text is empty or null, so it's not all numbers.
        }

        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false; // The text contains a non-numeric character, so it's not all numbers.
            }
        }

        return true; // The text is all numbers.
    }
    public  List<GradedExam> getGradedExamsByPlannedExamId(List<GradedExam> gradedExams, int plannedExamId) {
        List<GradedExam> matchingGradedExams = new ArrayList<>();

        for (GradedExam gradedExam : gradedExams) {
            if (gradedExam.getPlannedExam().getId() == plannedExamId) {
                matchingGradedExams.add(gradedExam);
            }
        }

        return matchingGradedExams;
    }
    public String formatGradedExams(List<GradedExam> gradedExams) {
        StringBuilder result = new StringBuilder();
if(gradedExams.isEmpty()){
    return "There Are No Grades In This Exam";
}
        // Get all the grades
        List<Double> grades = gradedExams.stream()
                .map(GradedExam::getGrade)
                .collect(Collectors.toList());

        // Calculate the average
        double average = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        // Calculate the standard deviation
        double variance = grades.stream()
                .map(grade -> Math.pow(grade - average, 2))
                .mapToDouble(Double::doubleValue).average().orElse(0.0);
        double standardDeviation = Math.sqrt(variance);

        // Calculate the median
        Collections.sort(grades);
        double median;
        int size = grades.size();
        if (size % 2 == 0) {
            median = (grades.get((size - 1) / 2) + grades.get(size / 2)) / 2.0;
        } else {
            median = grades.get(size / 2);
        }

        // Append the statistics to the result
        result.append(String.format("Average: %.2f\n", average));
        result.append(String.format("Standard Deviation: %.2f\n", standardDeviation));
        result.append(String.format("Median: %.2f\n\n", median));

        // Append the student details
        for (GradedExam exam : gradedExams) {
            String formattedLine = String.format("student name: %s  student id: %s  exam code: %s  grade: %.2f\n",
                    exam.getStudent().getName(), exam.getStudent().getId(), exam.getPlannedExam().getId(), exam.getGrade());
            result.append(formattedLine);
        }

        return result.toString();
    }
    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
    public  int checkIfTextIsNumber(String text) {
        // Check if the text is null or empty
        if (text == null || text.isEmpty()) {
            return 0;
        }
        for (char c : text.toCharArray()) {
            // If any character is not a digit, return 0
            if (!Character.isDigit(c)) {
                return 0;
            }

        }
        return 1;

    }
    public GradedExam findGradedExamById(List<GradedExam> gradedExams, int id) {
        for (GradedExam gradedExam : gradedExams) {
            if (gradedExam.getPlannedExam().getId() == id) {
                return gradedExam;
            }
        }
        return null; // Return null if no exam with the specified ID is found
    }

    public void setContent(String pageName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(pageName + ".fxml"));
        if(pageName.equals("add_exam"))
        {
            AddExamController x1=new AddExamController();
            x1.setApp(this);
            fxmlLoader.setController(x1);

        }
        if(pageName.equals("add_question"))
        {
            AddQuestionController x2 = new AddQuestionController();
            x2.setApp(this);
            fxmlLoader.setController(x2);

        }
        if(pageName.equals("TViewGrades")) {
TGradesController xz1=new TGradesController();
xz1.setApp(this);
            fxmlLoader.setController(xz1);

        }
            if(pageName.equals("do_exam"))
        {
            DoExamController x3 = new DoExamController();
            x3.setApp(this);
            fxmlLoader.setController(x3);

        }

        if(pageName.equals("approve_grades"))
        {
            ApproveGradeController x3 = new ApproveGradeController();
            x3.setApp(this);
            fxmlLoader.setController(x3);

        }
        if(pageName.equals("edit_exam"))
        {
            EditExamController x4 = new EditExamController();
            x4.setApp(this);
            fxmlLoader.setController(x4);
        }
        if(pageName.equals("edit_question"))
        {
            EditQuestionController x5 = new EditQuestionController();
            x5.setApp(this);
            fxmlLoader.setController(x5);
        }
        if(pageName.equals("prepare_exam"))
        {
            eddController x6 = new eddController();
            x6.setApp(this);
            fxmlLoader.setController(x6);
        }

        if(pageName.equals("ExtraTimeApproval"))
        {
            ExtraTimeApprovalController x8 = new ExtraTimeApprovalController();
            x8.setApp(this);
            fxmlLoader.setController(x8);
        }
        if(pageName.equals("ManualExam"))
        {
            ManualExamController x9 = new ManualExamController();
            x9.setApp(this);
            fxmlLoader.setController(x9);
        }
        if(pageName.equals("onlineExamExecution"))
        {
            OnlineExamController xy1 = new OnlineExamController();
            xy1.setApp(this);
            fxmlLoader.setController(xy1);
        }

        if(pageName.equals("principal_menu"))
        {
            PrincipalMenuController xy3 = new PrincipalMenuController();
            xy3.setApp(this);
            fxmlLoader.setController(xy3);
        }
        if(pageName.equals("principal_quetions"))
        {
            PrincipalViewQuetions xy4 = new PrincipalViewQuetions();
            xy4.setApp(this);
            fxmlLoader.setController(xy4);
        }
        if(pageName.equals("PrincipleExams"))
        {
            PrincipleExamsController xy5 = new PrincipleExamsController();
            xy5.setApp(this);
            fxmlLoader.setController(xy5);
        }
        if(pageName.equals("PViewGrades")) {
PGradesController xx1=new PGradesController();
xx1.setApp(this);
fxmlLoader.setController(xx1);

        }
            if(pageName.equals("PrincipleGrades"))
        {
            PrincipleGrades xy6 = new PrincipleGrades();
            xy6.setApp(this);
            fxmlLoader.setController(xy6);
        }
        if(pageName.equals("request_time_extention"))
        {
            ExtraTimeRequestController xy7 = new ExtraTimeRequestController();
            xy7.setApp(this);
            fxmlLoader.setController(xy7);
        }
        if(pageName.equals("student_menu"))
        {
            StudentMenuController xy8 = new StudentMenuController(this);
            xy8.setApp(this);
            fxmlLoader.setController(xy8);
        }
        if(pageName.equals("teacher_menu"))
        {
            TeacherMenuController xy9 = new TeacherMenuController();
            xy9.setApp(this);
            fxmlLoader.setController(xy9);
        }
        if(pageName.equals("view_questions"))
        {
            TeacherQuestionsController y1 = new TeacherQuestionsController();
            y1.setApp(this);
            fxmlLoader.setController(y1);
        }
        if(pageName.equals("view_exam"))
        {
            ViewExamController y2 = new ViewExamController();
            y2.setApp(this);
            fxmlLoader.setController(y2);
        }
        if(pageName.equals("view_grades"))
        {
            ViewExamGradeController y3 = new ViewExamGradeController();
            y3.setApp(this);
            fxmlLoader.setController(y3);
        }
        if(pageName.equals("viewData"))
        {
            ViewDataController y4 = new ViewDataController();
            y4.setApp(this);
            fxmlLoader.setController(y4);
        }
        if(pageName.equals("viewExamGrades"))
        {
            ViewExamGradeController y5 = new ViewExamGradeController();
            y5.setApp(this);
            fxmlLoader.setController(y5);
        }


        // student_menu //
        scene = new Scene(fxmlLoader.load());
        appstage.setScene(scene);
        appstage.show();
    }

    static void setRoot(String fxml) throws IOException {
        Parent parent=loadFXML(fxml);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId()
    {
        return this.id;
    }

    @Override
    public void stop() throws Exception {
        // TODO Auto-generated method stub
        SimpleClient.getClient().sendToServer("setlogout:"+id);
        super.stop();
    }
    // public List<String> teacher_get_subjects()
    //{
    // return this.teacher.getSubjects();
    //}



    public static void main(String[] args) {
        launch();
    }

}