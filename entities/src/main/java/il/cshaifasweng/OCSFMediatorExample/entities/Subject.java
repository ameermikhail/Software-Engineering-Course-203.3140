package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @OneToMany(mappedBy = "subject",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<Course>courses;
    @Column(name="subject_name")
    String name;
    @OneToMany(mappedBy = "subject", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Question> questions;
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Exam> exams;
    @ManyToMany(mappedBy = "subjects",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Teacher>Teachers;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<PlannedExam> PlannedExams;
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Exam> Exams;
    public Subject(int id) {
        this.id = id;
        this.questions = new ArrayList<>();
        this.exams = new ArrayList<>();
        this.PlannedExams = new ArrayList<>();
    }
    public void addCourse(Course course) {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        courses.add(course);
        course.setSubject(this);
    }

    public void addQuestion(Question question) {
        if (questions == null) {
            questions = new ArrayList<>();
        }
        questions.add(question);
    }

    public void addExam(Exam exam) {
        if (exams == null) {
            exams = new ArrayList<>();
        }
        exams.add(exam);
        exam.setSubject(this);
    }

    public void addPlannedExam(PlannedExam plannedExam) {
        if (PlannedExams == null) {
            PlannedExams = new ArrayList<>();
        }
        PlannedExams.add(plannedExam);
    }

    public void addTeacher(Teacher teacher) {
        if (Teachers == null) {
            Teachers = new ArrayList<>();
        }
        Teachers.add(teacher);
    }


    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Teacher> getTeachers() {
        return Teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        Teachers = teachers;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
    public List<String>GetQuestionsNames()
    {
        List<String>QuestionsNames=new ArrayList<>();
        for(Question question:this.getQuestions())
            QuestionsNames.add(String.valueOf(question.getQuestionId()));
        return QuestionsNames;
    }
    public int IsExamIdTaken(String id)
    {

        for(Exam exam:this.getExams()) {
            if (exam.getId() == id) return 1;
        }
        return 0;
    }
    public int IsPlannedExamIdTaken(String id)
    {

        for(PlannedExam exam:this.getPlannedExams()) {
            if (exam.getId() == Integer.parseInt(id)) return 1;
        }
        return 0;
    }
    public Question GetQuestionWithId(String id)
    {
        for(Question question:this.getQuestions()) {
            if (question.getQuestionId() == Integer.parseInt(id))
                return question;
        }
 return null;
    }
    public List<String>getCoursesNames() {


        List<String> courses_names = new ArrayList<>();

        List<Course> subject_courses = this.getCourses();
        for (Course course : subject_courses) {
            courses_names.add(String.valueOf(course.getCourse_id()));

        }
        return courses_names;

    }
    public Exam GetExamWithId(String id)
    {
        for(Exam exam:this.getExams())
        {
            if(exam.getId().equals(id))return exam;
        }
        return null;
    }


    public Course GetcourseWithId(String id)
    {

        for(Course course:this.getCourses())
            if(course.getCourse_id()==Integer.parseInt(id))return course;
return null;
    }


    public Subject() {
        this.questions = new ArrayList<>();
        this.exams = new ArrayList<>();
        this.PlannedExams = new ArrayList<>();
    }

    // Getters and Setters for the fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }
    public List<String> getQuestionIds() {
        List<String> questionIds = new ArrayList<>();

        for (Question question : questions) {
            questionIds.add(String.valueOf(question.getQuestionId()));
        }

        return questionIds;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<PlannedExam> getPlannedExams() {
        return PlannedExams;
    }

    public void setPlannedExams(List<PlannedExam> PlannedExams) {
        this.PlannedExams = PlannedExams;
    }


}
