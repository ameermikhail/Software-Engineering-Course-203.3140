package il.cshaifasweng.OCSFMediatorExample.entities;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "plannedexams")
public class PlannedExam implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
@Column(name="name")
private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name="IsFinished")
private int finished;

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Column(name = "start_time_column")
    private LocalDateTime startTime;
    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;




    @Column(name = "exam_type")
    private String examType;
@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
@JoinColumn(name = "subject_id")
private Subject subject;



    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "course_id")
    private Course course;
    @OneToMany(mappedBy = "plannedExam", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<GradedExam> GradedExams;

    public PlannedExam() {
    }
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public PlannedExam(Teacher teacher, Exam exam,LocalDateTime StartTime , String examType) {
        this.teacher = teacher;
        this.exam = exam;
        this.startTime=StartTime;
       // this.examDuration = examTime;
        this.examType = examType;
    }
    public PlannedExam(int id,Teacher teacher, Exam exam,Subject subject,Course course,LocalDateTime StartTime , String examType) {
        this.teacher = teacher;
        this.exam = exam;
        this.startTime=StartTime;
        this.subject=subject;
        this.course=course;
        // this.examDuration = examTime;
        this.id=id;
        this.examType = examType;
    }

    // Getters and Setters for the fields

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }




    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public List<GradedExam> getGradedExams() {
        return GradedExams;
    }

    public void setGradedExams(List<GradedExam> gradedExams) {
        GradedExams = gradedExams;
    }
    public void addGradedExam(GradedExam gradedExam) {
        if (this.GradedExams == null) {
            this.GradedExams = new ArrayList<>();
        }
        this.GradedExams.add(gradedExam);
    }


}
