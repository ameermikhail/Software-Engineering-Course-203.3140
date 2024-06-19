package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
@Entity
@Table(name = "gradedexams")
public class GradedExam implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;



    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="course_id")
    Course course;
    @Column(name="finished")
    int IsFinished;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "student_id")
    private Student student;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="teacher_id")
    private Teacher teacher;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "planned_exam_id"  )
    private PlannedExam plannedExam;
    @ElementCollection
    @CollectionTable(name = "graded_exam_answers", joinColumns = @JoinColumn(name = "graded_exam_id"))
    @Column(name = "answers")
    private List<Integer> Studentanswers;
    @Column(name = "corrected")
    int corrected=0;
    @Column(name = "Explanation")
    private String GradeChangeExplanation;

    public String getGradeChangeExplanation() {
        return GradeChangeExplanation;
    }

    public void setGradeChangeExplanation(String gradeChangeExplanation) {
        GradeChangeExplanation = gradeChangeExplanation;
    }
    @Column(name = "duration")
    private int examDuration;
    @Column(name = "totalPoints")
    private int totalPoints;
    @Column(name = "earnedPoints")

    private int earnedPoints;
    @Column(name = "Grade")

    private double grade;
    public GradedExam()
    {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorrected() {
        return corrected;
    }

    public void setCorrected(int corrected) {
        this.corrected = corrected;
    }

    public int getExamDuration() {
        return examDuration;
    }

    public void setExamDuration(int examDuration) {
        this.examDuration = examDuration;
    }

    public GradedExam(Student student, Teacher teacher, PlannedExam exam,List<Integer> Studentanswers ) {
        this.student = student;
        this.teacher = teacher;
        this.plannedExam = exam;
        this.Studentanswers = Studentanswers;
        this.totalPoints = 0;
        this.earnedPoints = 0;
        this.grade = 0.0;
    }
    public GradedExam(Student student, Teacher teacher, PlannedExam exam ) {
        this.student = student;
        this.teacher = teacher;
        this.plannedExam = exam;
        this.totalPoints = 0;
        this.earnedPoints = 0;
        this.grade = 0.0;
    }


    public String generateExamText() {
        StringBuilder text = new StringBuilder();
        List<Integer>points=this.plannedExam.getExam().getQuestionPoints();
        List<Question>questions=this.getPlannedExam().getExam().getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question currQuestion=questions.get(i);
            Question question = questions.get(i);

            text.append("Question ").append(i + 1).append(": ").append(question.getQuestionText())
                    .append(" (").append(points.get(i)).append(" Points)\n");

            int selectedAnswerIndex = Studentanswers.get(i);
            List<String>answers=question.getAnswers();
            // Print the student's answer and its correctness
            if (selectedAnswerIndex >= 0 && selectedAnswerIndex < answers.size()) {
                String selectedAnswer = answers.get(selectedAnswerIndex);
                int correctAnswerIndex = question.getCorrectAnswer();
                String correctness = (selectedAnswerIndex == correctAnswerIndex) ? "Correct" : "Wrong";
                String correctAnswer = answers.get(correctAnswerIndex);

                text.append("Answer: ").append(selectedAnswer).append(" (").append(correctness).append(")")
                        .append(" - Correct answer: ").append(correctAnswer).append("\n");
            } else {
                text.append("No answer provided\n");
            }

            text.append("\n");
        }

        return text.toString();
    }
    public void gradeExam() {
        List<Question> questions = plannedExam.getExam().getQuestions();
        List<Integer> questionPoints = plannedExam.getExam().getQuestionPoints();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            //List<String>QuestionAnswers=question.getAnswers();
           // String studentAnswer = Studentanswers.get(Studentanswers.get(i));
            int points = questionPoints.get(i);
            totalPoints += points;

            boolean isCorrect = Studentanswers.get(i)==(question.getCorrectAnswer());
            System.out.println("Answers"+Studentanswers.get(i)+"xxx"+question.getCorrectAnswer());
            if (isCorrect) {

                System.out.println("its correct:"+String.valueOf(i));
                earnedPoints += points;
            }

        }

        // Calculate the grade as a percentage
        if (totalPoints > 0) {
            grade = (double) earnedPoints / totalPoints * 100;
        }
    }

    public String saveStudentExamPaper() {
        StringBuilder gradedExamContent = new StringBuilder();
        List<Question>questions=this.plannedExam.getExam().getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            Question question=this.plannedExam.getExam().getQuestions().get(i);
            List<String>QuestionAnswers=question.getAnswers();
            List<Integer>QuestionsPoints=this.getPlannedExam().getExam().getQuestionPoints();
            String studentAnswer = QuestionAnswers.get(Studentanswers.get(i));
            boolean isCorrect = Studentanswers.get(i)==question.getCorrectAnswer();
            int points = QuestionsPoints.get(i);
            gradedExamContent.append("Question: ").append(question.getQuestionText()).append(System.lineSeparator());

            List<String> answers = question.getAnswers();
            String corect_answer=answers.get(question.getCorrectAnswer());

            for (String answer : answers) {
                if (studentAnswer.equalsIgnoreCase(answer)) {
                    if (isCorrect) {
                        gradedExamContent.append(answer).append(" (Correct)").append(System.lineSeparator());
                    } else {
                        gradedExamContent.append(answer).append(" (Incorrect)").append(System.lineSeparator());
                    }
                } else if (corect_answer.equalsIgnoreCase(answer)) {
                    gradedExamContent.append(answer).append(" (Correct)").append(System.lineSeparator());
                } else {
                    gradedExamContent.append(answer).append(System.lineSeparator());
                }
            }

            if (!isCorrect) {
                gradedExamContent.append("Your Answer: ").append(studentAnswer).append(System.lineSeparator());
                gradedExamContent.append("Correct Answer: ").append(question.getCorrectAnswer()).append(System.lineSeparator());
            }

            gradedExamContent.append(System.lineSeparator());
        }

        return gradedExamContent.toString();
    }
    public void DoneCorrecting()
    {
        this.corrected=1;
    }
    public int getIsFinished() {
        return IsFinished;
    }

    public void setIsFinished(int isFinished) {
        IsFinished = isFinished;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public PlannedExam getPlannedExam() {
        return plannedExam;
    }

    public void setPlannedExam(PlannedExam plannedExam) {
        this.plannedExam = plannedExam;
    }


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Integer> getStudentanswers() {
        return Studentanswers;
    }

    public void setStudentanswers(List<Integer> studentanswers) {
        Studentanswers = studentanswers;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public void setEarnedPoints(int earnedPoints) {
        this.earnedPoints = earnedPoints;
    }

    public double getGrade() {
        return grade;
    }


    public void setGrade(double grade) {
        this.grade = grade;
    }
    public void AddTime(int timeAddition)
    {
        if(IsFinished==0)
            this.examDuration+=timeAddition;
    }

    // ...
}
