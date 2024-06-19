package il.cshaifasweng.OCSFMediatorExample.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students_tb")
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToMany( fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST , mappedBy = "student")
    private List<GradedExam> gradedExams;
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public Student() {
        this.courses = new ArrayList<>();
        this.gradedExams = new ArrayList<>();
    }

    public Student(int id, String name) {
        this();
        this.id = id;
        this.name = name;
    }
    public PlannedExam getPlannedExamById(int searchId) {
        for (Course course : courses) {
            for (PlannedExam plannedExam : course.getCourse_planned_exams()) {
                if (plannedExam.getId() == searchId) {  // Assuming PlannedExam has a method getId()
                    return plannedExam;
                }
            }
        }
        return null;
    }

    public Student(int id, String name, ArrayList<GradedExam> grades) {
        this(id, name);
        this.gradedExams = grades;
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<GradedExam> getGradedExams() {
        return gradedExams;
    }

    public void setGradedExams(List<GradedExam> gradedExams) {
        this.gradedExams = gradedExams;
    }

    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new ArrayList<>();
        }
        this.courses.add(course);
    }

    public void addGradedExam(GradedExam gradedExam) {
        if (this.gradedExams == null) {
            this.gradedExams = new ArrayList<>();
        }
        this.gradedExams.add(gradedExam);
    }
}
