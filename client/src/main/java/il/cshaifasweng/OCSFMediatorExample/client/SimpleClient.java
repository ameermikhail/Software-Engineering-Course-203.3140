package il.cshaifasweng.OCSFMediatorExample.client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;

import javafx.fxml.FXML;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {
int islist=0;
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	public void initialize() throws IOException {
		EventBus.getDefault().register(this);
	}
	@Override
	protected void handleMessageFromServer(Object msg) {
		System.out.println("simplclient message from server");
if(msg instanceof String)
{
	String message=(String )msg;
	if(message.startsWith("addtime"))
	{
		System.out.println("addtime send from simple client");
		EventBus.getDefault().post(msg);

	}
}
		if (msg instanceof List<?> list) {
			System.out.println("simpleclient its list");
islist=1;
			if (!list.isEmpty()) {
				System.out.println("list not empty");
				Object firstElement = list.get(0);
				if (firstElement instanceof Student) {
					System.out.println("message send through bus");
					//List<Student> studentsList = (List<Student>) msg;
					//EventBus.getDefault().post(studentsList);
				} else if (firstElement instanceof Exam) {
					List<Exam> ExamsList = new ArrayList<>();
					for (Object obj : list) {
						if (obj instanceof Exam) {
							ExamsList.add((Exam) obj); // Type cast each object to Question
						}

					}
					EventBus.getDefault().post(ExamsList);

				}else if (firstElement instanceof PlannedExam) {
					List<PlannedExam> gradesList = new ArrayList<>();
					List<?> listt = (List<?>) msg;
					System.out.println("its planned"+String.valueOf(list.size()));
					for (Object obj : listt) {
						gradesList.add((PlannedExam) obj);

					}
					EventBus.getDefault().post(gradesList);
				}
				else if (firstElement instanceof GradedExam) {
					List<GradedExam> gradesList = new ArrayList<>();
					List<?> listt = (List<?>) msg;
System.out.println("its gradesss"+String.valueOf(list.size()));
					for (Object obj : listt) {
							gradesList.add((GradedExam) obj);

					}
					EventBus.getDefault().post(gradesList);
				} else if (firstElement instanceof Course) {
					List<Course> courseList = new ArrayList<>();

					for (Object obj : list) {
							courseList.add((Course) obj); // Type cast each object to Question

					}
					EventBus.getDefault().post(courseList);

				}


				else if (firstElement instanceof Question) {
					List<Question> questionList = new ArrayList<>();
					for (Object obj : list) {
							questionList.add((Question) obj); // Type cast each object to Question


						EventBus.getDefault().post(questionList);

					}

				} else if (list.get(0) instanceof Subject) {
					System.out.println("firstleemeeent");
					List<Subject> subjectslist = new ArrayList<>();
					for (Object obj : list) {
						if (obj instanceof Subject) {
							subjectslist.add((Subject) obj); // Type cast each object to Question
						}

					}
					EventBus.getDefault().post(subjectslist);

				}
				else if (list.get(0) instanceof Time_Extention_request) {
					System.out.println("firstleemeeent");
					List<Time_Extention_request> subjectslist = new ArrayList<>();
					for (Object obj : list) {
							subjectslist.add((Time_Extention_request) obj); // Type cast each object to Question


					}
					EventBus.getDefault().post(subjectslist);

				}
			}
		}
		 else if (msg instanceof PlannedExam) {
			System.out.println("its Planned");
			EventBus.getDefault().post((PlannedExam)msg);


		}

		else if (msg instanceof String) {
					System.out.println("client got this message from server" + (String) msg);
					System.out.println((String) msg);
					EventBus.getDefault().post((String) msg);

				}
				else if (msg instanceof Student) {
					System.out.println("its student - sC");
					EventBus.getDefault().post((Student) msg);
				}
		else if (msg instanceof Question) {
			System.out.println("its question - sC");
			EventBus.getDefault().post((Question) msg);
		}
			else 	if (msg instanceof Teacher) {
					System.out.println("its teacher - sC");

					EventBus.getDefault().post((Teacher) msg);
				}
		else if (msg instanceof Subject) {
			System.out.println("its subject - sC");

			EventBus.getDefault().post((Subject) msg);
		}
				else if (msg instanceof Principal)
					EventBus.getDefault().post((Principal) msg);
				else if (msg instanceof Exam)
					System.out.println("exam posted");
					EventBus.getDefault().post((Exam) msg);

System.out.println("simplclientend reached");

		}


	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3000);
			client.connectionEstablished();
		}
		return client;
	}
}
