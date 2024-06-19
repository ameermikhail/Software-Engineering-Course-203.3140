package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id
    @Column(name = "course_id")
    private int course_id;
    @Column(name="course_name")
    String name;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "subject_id" )
    private Subject subject;

    @OneToMany(mappedBy = "courses",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<Student> course_students;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<PlannedExam> course_planned_exams;

    @OneToMany(mappedBy = "course",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<GradedExam> course_graded_exams;

    public Course() {
    }

    public Course(int course_id) {
        this.course_id = course_id;
    }

    public void addStudent(Student student) {
        if (course_students == null) {
            course_students = new ArrayList<>();
        }
        course_students.add(student);
    }


    public void addPlannedExam(PlannedExam plannedExam) {
        if (course_planned_exams == null) {
            course_planned_exams = new ArrayList<>();
        }
        course_planned_exams.add(plannedExam);
        plannedExam.setCourse(this);
    }

    public void addGradedExam(GradedExam gradedExam) {
        if (course_graded_exams == null) {
            course_graded_exams = new ArrayList<>();
        }
        course_graded_exams.add(gradedExam);
        gradedExam.setCourse(this);
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Student> getCourse_students() {
        return course_students;
    }

    public void setCourse_students(List<Student> course_students) {
        this.course_students = course_students;
    }

    public Teacher getCourse_teacher() {
        return teacher;
    }

    public void setCourse_teacher(Teacher course_teacher) {
        this.teacher = course_teacher;
    }

    public List<PlannedExam> getCourse_planned_exams() {
        return course_planned_exams;
    }

    public void setCourse_planned_exams(List<PlannedExam> course_planned_exams) {
        this.course_planned_exams = course_planned_exams;
    }

    public List<GradedExam> getCourse_graded_exams() {
        return course_graded_exams;
    }

    public void setCourse_graded_exams(List<GradedExam> course_graded_exams) {
        this.course_graded_exams = course_graded_exams;
    }
    public boolean isStudentInCourse(Student student) {
        return course_students.contains(student);
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
