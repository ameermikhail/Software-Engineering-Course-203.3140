package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "time_extentions_request")

public class Time_Extention_request implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name="isDone")
    private int isdone;

    public int getIsdone() {
        return isdone;
    }

    public void setIsdone(int isdone) {
        this.isdone = isdone;
    }

    @Column(name="TimeAddition")

    private int TimeAddition;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "planned_exam_id",referencedColumnName = "id")
    PlannedExam plannedExam;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "teacher_id", nullable = false)
    Teacher teacher;

    public Time_Extention_request() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeAddition() {
        return TimeAddition;
    }

    public void setTimeAddition(int timeAddition) {
        TimeAddition = timeAddition;
    }

    public PlannedExam getPlannedExam() {
        return plannedExam;
    }

    public void setPlannedExam(PlannedExam plannedExam) {
        this.plannedExam = plannedExam;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Time_Extention_request(int timeAddition, PlannedExam plannedExam, Teacher teacher) {
        TimeAddition = timeAddition;
        this.plannedExam = plannedExam;
        this.teacher = teacher;
    }

    public void Accept() {

        List<GradedExam> currentExams = this.plannedExam.getGradedExams();
        for (GradedExam exam : currentExams) {
            exam.AddTime(this.TimeAddition);
        }
    }
}