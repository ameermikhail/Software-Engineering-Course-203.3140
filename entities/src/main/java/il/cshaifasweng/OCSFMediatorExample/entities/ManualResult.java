package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ManualResults")
public class ManualResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private int examId;

    @Column(name = "student_id")
    private int studentId;

    @Column(name = "exam_text", length = 1000) // You can adjust the length according to your needs.
    private String examText;

    // Constructors
    public ManualResult() {
    }

    public ManualResult(int studentId,int examId, String examText) {
        this.studentId = studentId;
        this.examText = examText;
        this.examId=examId;
    }

    // Getters and setters
    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getExamText() {
        return examText;
    }

    public void setExamText(String examText) {
        this.examText = examText;
    }

    // hashCode, equals, and toString methods can also be added.
}
