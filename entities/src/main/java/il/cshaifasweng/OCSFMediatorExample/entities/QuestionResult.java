package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "question_result")
public class QuestionResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @Column(name = "student_answer")
    private String studentAnswer;

    @Column(name = "correct")
    private boolean correct;

    @Column(name = "points")
    private int points;

    @ManyToOne
    @JoinColumn(name = "planned_exam_id")
    private PlannedExam plannedExam;

    // constructors, getters and setters
}
