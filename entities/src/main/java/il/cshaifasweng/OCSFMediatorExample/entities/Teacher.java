package il.cshaifasweng.OCSFMediatorExample.entities;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    String name;
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<Course> courses;
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    List<Exam> exams;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "teacherXXSubjects",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<Subject> subjects;
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<GradedExam> gradedExams;
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<PlannedExam> PlannedExams;
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Time_Extention_request> timeExtentionRequests;

    public List<GradedExam> getGradedExams() {
        return gradedExams;
    }

    public void setGradedExams(List<GradedExam> gradedExams) {
        this.gradedExams = gradedExams;
    }
    //add the ting


    public Teacher() {
        this.subjects = new ArrayList<>();
        this.PlannedExams = new ArrayList<>();
    }

    // Getters and Setters for the fields
    public PlannedExam GetPlannedExamWithId(int id) {

        for (PlannedExam plannedExam : this.getPlannedExams()) {
            if (plannedExam.getId() == id)
                return plannedExam;
        }
        return null;
    }


    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new ArrayList<>();
        }
        this.courses.add(course);
    }

    public void addExam(Exam exam) {
        if (this.exams == null) {
            this.exams = new ArrayList<>();
        }
        this.exams.add(exam);
    }

    public void addSubject(Subject subject) {
        if (this.subjects == null) {
            this.subjects = new ArrayList<>();
        }
        this.subjects.add(subject);
    }

    public void addGradedExam(GradedExam gradedExam) {
        if (this.gradedExams == null) {
            this.gradedExams = new ArrayList<>();
        }
        this.gradedExams.add(gradedExam);
    }

    public void addPlannedExam(PlannedExam plannedExam) {
        if (this.PlannedExams == null) {
            this.PlannedExams = new ArrayList<>();
        }
        this.PlannedExams.add(plannedExam);
    }

    public void addTimeExtensionRequest(Time_Extention_request timeExtentionRequest) {
        if (this.timeExtentionRequests == null) {
            this.timeExtentionRequests = new ArrayList<>();
        }
        this.timeExtentionRequests.add(timeExtentionRequest);
    }

    public List<String> getCoursesIds() {
        List<String> coursesIds = new ArrayList<>();

        // Check if the courses list is not empty
        if (courses != null && !courses.isEmpty()) {
            // Iterate through the teacher's courses
            for (Course course : courses) {
                // Add the course ID to the list as a string
                coursesIds.add(String.valueOf(course.getCourse_id()));
            }
        }

        return coursesIds;
    }

// Getters and Setters for the other lists:

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Time_Extention_request> getTimeExtentionRequests() {
        return timeExtentionRequests;
    }

    public void setTimeExtentionRequests(List<Time_Extention_request> timeExtentionRequests) {
        this.timeExtentionRequests = timeExtentionRequests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public Subject getsubjectWithNumber(int subjectNumber) {
        for (Subject subject : getSubjects()) {
            if (subject.getId() == subjectNumber) return subject;
        }
        return null;
    }

    public List<Question> GetAllQuestions() {
        List<Question> questions = new ArrayList<>();
        for (Subject sub : subjects) {
            for (Question ques : sub.getQuestions()) {
                questions.add(ques);
            }

        }
        return questions;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    public List<String> GetSubjectsNamesandIds() {

        List<String> subjects_names = new ArrayList<>();

        List<Subject> teacher_subjects = this.getSubjects();
        if (!teacher_subjects.isEmpty()) {
            for (Subject subject : teacher_subjects) {
                subjects_names.add(String.valueOf(subject.getId())+" "+subject.getName());

            }
        }
        return subjects_names;

    }
    public List<String> GetSubjectsNames() {

        List<String> subjects_names = new ArrayList<>();

        List<Subject> teacher_subjects = this.getSubjects();
        if (!teacher_subjects.isEmpty()) {
            for (Subject subject : teacher_subjects) {
                subjects_names.add(String.valueOf(subject.getId())+" "+subject.getName());

            }
        }
        return subjects_names;

    }

    public List<PlannedExam> getPlannedExams() {
        return PlannedExams;
    }

    public void setPlannedExams(List<PlannedExam> PlannedExams) {
        this.PlannedExams = PlannedExams;
    }

    public List<String> getExamIdsInSubjects() {
        List<String> examIdsInSubjects = new ArrayList<>();
        List<Exam> examsInSubjects = getExamsInSubjects();

        // Extract the IDs of exams as strings and add them to the list
        for (Exam exam : examsInSubjects) {
            examIdsInSubjects.add(String.valueOf(exam.getId()));
        }

        return examIdsInSubjects;
    }

    public List<String> getQuestionsinSubjectsIDDS() {
        List<String> QuestrionIdsInSubjects = new ArrayList<>();
        List<Question> quesInSubjects = getQuestionsInsubjects();

        // Extract the IDs of exams as strings and add them to the list
        for (Question ques : quesInSubjects) {
            QuestrionIdsInSubjects.add(String.valueOf(ques.getQuestionId())+" "+ques.getQuestionText());
        }

        return QuestrionIdsInSubjects;
    }

    public List<Exam> getExamsInSubjects() {
        List<Exam> examsInSubjects = new ArrayList<>();

        // Iterate through the teacher's subjects
        for (Subject subject : subjects) {
            // Get the exams associated with the subject
            List<Exam> subjectExams = subject.getExams();

            // Add the exams to the list if they are not already present
            for (Exam exam : subjectExams) {
                if (!examsInSubjects.contains(exam)) {
                    examsInSubjects.add(exam);
                }
            }
        }

        return examsInSubjects;
    }

    public GradedExam findGradedExamByStudentAndExamId(int studentId, int examId) {
        for (GradedExam gradedExam : gradedExams) {
            if (gradedExam.getStudent().getId() == studentId && gradedExam.getPlannedExam().getId() == examId) {
                return gradedExam;
            }
        }
        return null; // Return null if no matching graded exam is found
    }

    public Exam getExamById(String examId) {
        List<Exam> examsInSubjects = getExamsInSubjects();

        // Find the exam with the given ID
        for (Exam exam : examsInSubjects) {
            if (String.valueOf(exam.getId()).equals(examId)) {
                return exam;
            }
        }

        // If no exam with the given ID is found, return null
        return null;
    }

    public List<Question> getQuestionsInsubjects() {
        List<Question> QuesinSubject = new ArrayList<>();

        // Iterate through the teacher's subjects
        for (Subject subject : subjects) {
            // Get the exams associated with the subject
            List<Question> subquests = subject.getQuestions();

            // Add the exams to the list if they are not already present
            for (Question ques : subquests) {
                if (!QuesinSubject.contains(ques)) {
                    QuesinSubject.add(ques);
                }
            }
        }
        return QuesinSubject;
    }

}