package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrimaryController extends BaseController{

	@FXML
	private ListView<String> studentListView;

	@FXML
	private ListView<String> gradesListView;

	@FXML
	private TextField newGradeTextField;

	@FXML
	private Button updateButton;

	@FXML
	private Button showUpdatedButton;

	@FXML
	public void initialize() throws IOException {
		EventBus.getDefault().register(this);
		loadStudentList();
	}
	@FXML
	void showUpdatedGradesButtonAction(ActionEvent event) throws IOException {
		String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
			SimpleClient.getClient().sendToServer("GET_GRADES:" + selectedStudent);
		}


	@FXML
	void updateButtonAction(ActionEvent event) throws IOException {
		String selectedStudent = studentListView.getSelectionModel().getSelectedItem();
		String newGrade = newGradeTextField.getText();
		String selectedSubject = gradesListView.getSelectionModel().getSelectedItem();
		if(selectedSubject.startsWith("Math"))selectedSubject="Math";
		else selectedSubject="Science";
		if (selectedStudent != null && !newGrade.isEmpty() && selectedSubject != null) {
			SimpleClient.getClient().sendToServer("UPDATE_GRADE:" + selectedStudent+":"+selectedSubject+":"+newGrade);

		}
	}


	private void loadStudentList() throws IOException {
		SimpleClient.getClient().sendToServer("GET_STUDENT_LIST");
	}
	@FXML
	private void loadStudenGrades() throws IOException {
		String selectedItemText = studentListView.getSelectionModel().getSelectedItem();
		SimpleClient.getClient().sendToServer("GET_GRADES:"+selectedItemText);
	}

	@Subscribe
	public void handleEvent(Object event) {
		if (event instanceof List<?>) {
			List<?> list = (List<?>) event;

			if (!list.isEmpty()) {
				Object firstElement = list.get(0);

				if (firstElement instanceof Student) {
					handleStudentsList((List<Student>) list);
				}
			}
		}
	}

	private void handleStudentsList(List<Student> students) {
		System.out.println("message got to bus");
		List<String> studentNames = new ArrayList<>();
		for (Student student : students) {
			studentNames.add(student.getName());
		}
		studentListView.getItems().setAll(studentNames);
	}

	/*private void handleStudentGrades(List<Grade> grades) {
		List<String> gradesFormatted = new ArrayList<>();
		int flag = 0;
		for (Grade grade : grades) {
			String subject = grade.getExamSubject();
			String gradeString = subject + ": " + grade.getGrade();
			gradesFormatted.add(gradeString);
			flag = (flag + 1) % 2;
		}
		Platform.runLater(() -> {
			if (gradesListView.getItems() == null) {
				gradesListView.setItems(FXCollections.observableArrayList(gradesFormatted));
			} else {
				gradesListView.getItems().setAll(gradesFormatted);
			}
		});
	}*/







	}
