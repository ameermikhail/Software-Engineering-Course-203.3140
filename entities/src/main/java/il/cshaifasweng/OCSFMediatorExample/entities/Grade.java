package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "grades_tbb")
public class Grade implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int grade_id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @Column(name = "examSubject")
    private String examSubject;
    @Column(name = "grade")
    private double grade;

    public Grade() {
    }

    public Grade(String examSubject, double grade) {
        this.examSubject = examSubject;
        this.grade = grade;
    }

    public Grade(Student student, String examSubject, double grade) {
        this.student = student;
        this.examSubject = examSubject;
        this.grade = grade;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getExamSubject() {
        return examSubject;
    }

    public void setExamSubject(String examSubject) {
        this.examSubject = examSubject;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
