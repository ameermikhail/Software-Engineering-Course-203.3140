package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
public class Exam implements Serializable {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name="name")
    private String name;
    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "course_id")
    Course course;
    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @Column(name = "exam_duration")
    private int examDuration;



    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "exam_question",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id"))
    private List<Question> questions;

    @OneToMany(mappedBy = "exam", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<PlannedExam> PlannedExams;

    @Column(name = "instructions_for_students")
    private String instructionsForStudents;

    @Column(name = "instructions_for_teacher")
    private String instructionsForTeacher;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST )
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ElementCollection
    @CollectionTable(name = "question_points", joinColumns = @JoinColumn(name = "exam_id"))
    @Column(name = "points")
    private List<Integer> questionPoints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Exam() {
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(int examDuration) {
        this.examDuration = examDuration;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public Exam(String id) {
        this.id=id;
        this.questions = new ArrayList<>();
        this.PlannedExams = new ArrayList<>();
    }
    public void addPlannedExam(PlannedExam plannedExam) {
        if (PlannedExams == null) {
            PlannedExams = new ArrayList<>();
        }
        PlannedExams.add(plannedExam);
        plannedExam.setExam(this);
    }
    public Exam(Exam other) {
        this.id = other.id;
        this.subject = other.subject;
        this.questions = new ArrayList<>(other.questions);
        this.PlannedExams = new ArrayList<>(other.PlannedExams);
        this.instructionsForStudents = other.instructionsForStudents;
        this.instructionsForTeacher = other.instructionsForTeacher;
        this.teacher = other.teacher;
        this.questionPoints = new ArrayList<>(other.questionPoints);
    }

    public Exam(String id, String instructionsForStudents, String instructionsForTeacher, int teacherId, List<Integer> questionPoints) {
        this.id = id;
        this.questions = new ArrayList<>();
        this.instructionsForStudents = instructionsForStudents;
        this.instructionsForTeacher = instructionsForTeacher;
       // this.teacherId = teacherId;
        this.questionPoints = questionPoints;
    }
    public Exam(String id, String instructionsForStudents, String instructionsForTeacher, Teacher teacher,Subject subject,Course course,int duration)
    {
        this.id = id;
        this.teacher=teacher;
        this.subject=subject;
        this.questionPoints=new ArrayList<>();
        this.questions=new ArrayList<>();
        this.instructionsForStudents = instructionsForStudents;
        this.instructionsForTeacher = instructionsForTeacher;
        this.course=course;
        this.examDuration=duration;
        this.PlannedExams = new ArrayList<>();

    }
    public Exam(String id, String instructionsForStudents, String instructionsForTeacher, int teacherId, List<Integer> questionPoints, List<Question> questions) {
        this.id = id;
        this.questions = questions;
        this.instructionsForStudents = instructionsForStudents;
        this.instructionsForTeacher = instructionsForTeacher;
        //this.teacherId = teacherId;
        this.questionPoints = questionPoints;

        if (questions.size() != questionPoints.size()) {
            throw new IllegalArgumentException("Number of question points does not match the number of questions.");
        }
    }
    public List<String> getQuestionIds() {
        List<String> questionIds = new ArrayList<>();

        for (Question question : questions) {
            questionIds.add(String.valueOf(question.getQuestionId()));
        }

        return questionIds;
    }


    public int getQuestionPointsById(String questionId) {

        // Loop through each question to find the one with the given ID
        for (int i = 0; i < questions.size(); i++) {
            if (String.valueOf(questions.get(i).getQuestionId()).equals(questionId)) {
                return questionPoints.get(i);
            }
        }

        // If no question with the given ID is found, return -1 (indicating not found)
        return -1;
    }
    public void setQuestionPointsById(String questionId, int points) {


        // Loop through each question to find the one with the given ID
        for (int i = 0; i < questions.size(); i++) {
            if (String.valueOf(questions.get(i).getQuestionId()).equals(questionId)) {
                questionPoints.set(i, points);
                return;
            }
        }
    }

    // Function to remove a question and its points given its ID
    public boolean removeQuestionById(String questionId) {

        // Loop through each question to find the one with the given ID and remove it
        for (int i = 0; i < questions.size(); i++) {
            if (String.valueOf(questions.get(i).getQuestionId()).equals(questionId)) {
                questions.remove(i);
                questionPoints.remove(i);
                return true; // Question found and removed
            }
        }

        // If no question with the given ID is found, return false (indicating not found)
        return false;
    }
    public String generateExamText() {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int points = questionPoints.get(i);
            text.append("Question ").append(i + 1).append(": ").append(question.getQuestionText())
                    .append(" (").append(points).append(" Points)\n");
            List<String> answers = question.getAnswers();
            for (int j = 0; j < answers.size(); j++) {
                text.append("Answer ").append(j + 1).append(": ").append(answers.get(j)).append("\n");
            }
            text.append("\n");
        }
        return text.toString();
    }
    // Function to add a new question to the exam
    public void AddQuestion(Question question,int points)
    {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
        if (this.questionPoints == null) {
            this.questionPoints = new ArrayList<>();
        }
        this.questionPoints.add(points);


    }
    public void addQuestion(Question question,int points)
    {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        this.questions.add(question);
        if (this.questionPoints == null) {
            this.questionPoints = new ArrayList<>();
        }
        this.questionPoints.add(points);


    }


        // Getters and Setters for the fields

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<PlannedExam> getPlannedExams() {
        return PlannedExams;
    }

    public void setPlannedExams(List<PlannedExam> PlannedExams) {
        this.PlannedExams = PlannedExams;
    }

    public String getInstructionsForStudents() {
        return instructionsForStudents;
    }

    public void setInstructionsForStudents(String instructionsForStudents) {
        this.instructionsForStudents = instructionsForStudents;
    }

    public String getInstructionsForTeacher() {
        return instructionsForTeacher;
    }

    public void setInstructionsForTeacher(String instructionsForTeacher) {
        this.instructionsForTeacher = instructionsForTeacher;
    }
    public String GetExamWholeNumber()
    {
        return "";
    }

    public List<Integer> getQuestionPoints() {
        return questionPoints;
    }

    public void setQuestionPoints(List<Integer> questionPoints) {
        if (questions.size() != questionPoints.size()) {
            throw new IllegalArgumentException("Number of question points does not match the number of questions.");
        }

        this.questionPoints = questionPoints;
    }
}
