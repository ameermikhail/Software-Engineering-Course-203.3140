package il.cshaifasweng.OCSFMediatorExample.server;

import java.util.List;

public class ServerMessage {
    private int message_id;
    private List<String> studentsList;
    private List<Integer> studentGrades;

    public ServerMessage(int message_id, List<String> studentsList, List<Integer> studentGrades) {
        this.message_id = message_id;
        this.studentsList = studentsList;
        this.studentGrades = studentGrades;
    }

    public int getMessageId() {
        return message_id;
    }

    public void setMessageId(int message_id) {
        this.message_id = message_id;
    }

    public List<String> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(List<String> studentsList) {
        this.studentsList = studentsList;
    }

    public List<Integer> getStudentGrades() {
        return studentGrades;
    }

    public void setStudentGrades(List<Integer> studentGrades) {
        this.studentGrades = studentGrades;
    }
}
