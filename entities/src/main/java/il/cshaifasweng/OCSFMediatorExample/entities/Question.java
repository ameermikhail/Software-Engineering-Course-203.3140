package il.cshaifasweng.OCSFMediatorExample.entities;
import il.cshaifasweng.OCSFMediatorExample.entities.Subject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "questions")
public class Question implements Serializable {
    @Id
    @Column(name = "question_id")
    private int id;
    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "question_code")
    private int questionCode;
    @Column(name="question_text")
    private String questionText;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "questionsAnswers", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "answer")
    private List<String> answers;


    @Column(name = "correct_answer")
    private int correctAnswer;

    public Question() {
    }

    public Question(Subject subject, int questionCode,String Questiontext, List<String> answers, int correctAnswer) {
        this.subject = subject;
        this.questionCode = questionCode;
        this.id = subject.getId() * 10000 + questionCode;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.questionText=Questiontext;
    }

    public String QuestionToText2()
    {
        int i = 1;
        StringBuilder text = new StringBuilder("Question : " + this.getQuestionText() + "\n");

        for (String answer : getAnswers())
        {
            if (i == getCorrectAnswer())
            {
                text.append("answer ").append(i).append(" (Correct): ").append(answer).append("\n");
            }
            else
            {
                text.append("answer ").append(i).append(": ").append(answer).append("\n");
            }
            i++;
        }
        return text.toString();
    }

    // Getters and Setters for the fields
    public String QuestionToText()
    {
        int i=1;
        String text;
        text="Question :  "+this.getQuestionText()+"\n";
        for(String answer:getAnswers())
        {
            text+="answer "+i+" :  "+answer+"\n";
i++;
        }
        return text;

    }
    public int getQuestionId() {
        return id;
    }

    public void setQuestionId(int questionId) {
        this.id = questionId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    // Function to return the subject based on the first two digits of the questionId
    public Subject getSubject() {
        return this.subject;
    }

    public int getQuestionCode() {
        return this.questionCode;
    }
    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    // Function to return the questionId based on the third and fourth digits of the questionId
}
